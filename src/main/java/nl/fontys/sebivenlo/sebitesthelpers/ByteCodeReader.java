package nl.fontys.sebivenlo.sebitesthelpers;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.util.stream.Collectors.joining;
import java.util.stream.Stream;
import static nl.fontys.sebivenlo.sebitesthelpers.ByteCodeReader.ReaderState.INHEADER;

/**
 * Reads byte codes via javap invocation.
 *
 * This class reads the method body byte code lines and saves them in a map as
 * cache. It reads and collects the method byte code, and the federated classes.
 * Federated classes are classes that live in the same compilation unit. For
 * Maven projects that means that the have class files under target/classes.
 *
 * @author "Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}"
 */
public class ByteCodeReader {

    private final String clazzBaseName;

    final Map<String, List<String>> methodMap = new LinkedHashMap<>();
    final Map<String, Integer> codeSizeMap = new LinkedHashMap<>();
    final Set<String> federatedClassFiles = new LinkedHashSet<>();
    private String currentMethod;
    private int lastMethodLineNumber;
    private final AtomicInteger classByteCodeTotalSize = new AtomicInteger( 0 );

    /**
     * Create a reader and read the given class.
     *
     * @param aClass to read
     */
    private ByteCodeReader( Class<?> aClass ) {
        //this.clazz = aClass;
        this( classToFileNameBase( aClass ) );
    }

    private ByteCodeReader( String classNameBase ) {
        Objects.requireNonNull( classNameBase );
        clazzBaseName = classNameBase;
//        classFileName = clazzName + ".class";
        readCode();
    }

    public static ByteCodeReader readCode( String aClassName ) {

        ByteCodeReader earlier = readerCache.get( aClassName );
        if ( null != earlier ) {
            return earlier;
        } else {
            // will cache itself
            return new ByteCodeReader( aClassName );
        }

    }

    public static ByteCodeReader readCode( Class<?> aClass ) {
        return readCode( classToFileNameBase( aClass ) );
    }

    /**
     * Do the actual reading.
     *
     * @return this reader to enable a fluent interface.
     */
    private ByteCodeReader readCode() {
        try {
            String fqFileName = "target/classes/" + clazzBaseName.replaceAll( "\\.", "/" ) + ".class";
            ProcessBuilder pb = new ProcessBuilder( "javap", "-v", "-p", fqFileName );
            String javapOut = fqFileName + ".txt";
            File outFile = new File( javapOut );
            File outDir = outFile.getParentFile();
            outDir.mkdirs();// assume it does create dirs
            pb.redirectOutput( outFile );
            Process process = pb.start();
            process.waitFor();
            Path jPP = Paths.get( javapOut );
            if ( !Files.isReadable( jPP ) ) {
                throw new AssertionError( "cannot read class file with name '" + clazzBaseName + "'" );
            }
            readerState = INHEADER;
            Files.lines( jPP ).forEachOrdered( this::readLine );
        } catch ( IOException | InterruptedException ex ) {
            Logger.getLogger( ByteCodeReader.class.getName() ).log( Level.SEVERE, null, ex );
        }
        readerCache.put( clazzBaseName, this );
        return this;
    }

    /**
     * Get the byte code lines for the given method signature. Gets the class
     * and method using method object by converting the method into a simple
     * string representation of the signature, which is equal to the one
     * produced by javap.
     *
     * @param m the method.
     * @return the list of byte code for a method.
     */
    List<String> getMethodCode( Method m ) {
        String key = methodToSimpleSig( m );
        return readerCache.get( clazzBaseName ).methodMap.getOrDefault( key, Collections.EMPTY_LIST );
    }

    /**
     * Helper to convert a method to a string akin toe the signature produced by
     * javap.
     *
     * @param m method
     * @return the string
     */
    final String methodToSimpleSig( Method m ) {
        String mName = m.getName();
        Type[] genericParameterTypes = m.getGenericParameterTypes();

        String params = Arrays.stream( genericParameterTypes ).map( p -> p.getTypeName() ).collect( joining( ", " ) );
        String key = mName + '(' + params + ')';
        return key;
    }

    /**
     * Stream all code lines of this ByteCodeReader.
     *
     * @return all lines in a stream.
     */
    Stream<String> allClassCodeLines() {
        return readerCache.get( clazzBaseName ).methodMap.values().stream().flatMap( List::stream );
    }

    /**
     * The code is read suing a state machine that detects the sections in a
     * 'javap -v output.
     */
    enum ReaderState {
        /**
         * Before any code. Code starts with '{' and ends with '}'
         */
        INHEADER {
            @Override
            void readLine( ByteCodeReader ctx, String line ) {
                if ( line.startsWith( "{" ) ) {
                    ctx.readerState = INCLASSBODY;
                }
                Matcher m = ctx.classRefLine.matcher( line );
                if ( m.matches() ) {
                    String canonicalName = m.group( 2 );
//                    System.out.println( "reader canonicalName = " + canonicalName );
                    String federatedFileName = "target/classes/" + canonicalName + ".class";
                    if ( Files.exists( Path.of( federatedFileName ) ) ) {
                        ctx.federatedClassFiles.add( canonicalName );
                    }
                }
            }
        },
        /**
         * Inside the body we can read lines, but only after detecting a a
         * signature
         */
        INCLASSBODY {
            @Override
            void readLine( ByteCodeReader ctx, String line ) {
                Matcher m = ctx.methodSignature.matcher( line );
                if ( m.matches() ) {
                    String sig = m.group( 1 );
                    ctx.methodMap.put( sig, new ArrayList<>() );
                    ctx.currentMethod = sig;
                    ctx.readerState = ReaderState.INMETHODBODY;
                } else if ( "}".equals( line ) ) {
                    ctx.readerState = ReaderState.INTRAILER;
                }
            }
        },
        /**
         * After signature we read code lines until we hit an empty line.
         */
        INMETHODBODY {
            @Override
            void readLine( ByteCodeReader ctx, String line ) {
                Matcher m = ctx.byteCodeLine.matcher( line );
                if ( m.matches() ) {
                    ctx.lastMethodLineNumber = Integer.parseInt( m.group( 1 ) );
                    ctx.methodMap.get( ctx.currentMethod ).add( line );
                } else if ( line.isEmpty() ) {
                    // save last method code line read, which is a good match for the byte code size.
                    ctx.codeSizeMap.put( ctx.currentMethod, ctx.lastMethodLineNumber );
                    ctx.classByteCodeTotalSize.addAndGet( ctx.lastMethodLineNumber );
                    ctx.readerState = INCLASSBODY;
                }
            }

        },
        /**
         * Everything after '}' is trailer stuff.
         */
        INTRAILER {
            @Override
            void readLine( ByteCodeReader ctx, String line ) {
            }
        };

        abstract void readLine( ByteCodeReader ctx, String line );
    }

    private ReaderState readerState = INHEADER;
    //                                     2 spaces followed by word chars up to methodName(
    /**
     * Method signature as per javap output.
     */
    final Pattern methodSignature = Pattern.compile(
            "\\s{2}\\w.+?" // leading 2 spaces
            + "([_A-Za-z][_A-Za-z$0-9]*" // name starts with [_azAZ]
            + "\\(.*?\\)" // opening parens anything in  between, closing parens
            + ");$" // end of group, semicolon, end of line
    );
    final Pattern byteCodeLine = Pattern.compile(
            "\\s+?" // a bunch of spaces
            + "(\\d+)?:" // byte code number flush next to colon
            + "\\s\\w" // a space and the byte code word
            + ".+$" // anything untill the end of line
    );

    final Pattern classRefLine = Pattern.compile(
            "\\s+?" // a bunch of spaces
            + "#(\\d+)?\\s=\\sClass" // #refnum = Class
            + "\\s+#\\d+\\s+//\\s(\\S+)$" // some spaces #refnum spaces slashes space and (name) $

    );

    private void readLine( String s ) {
        readerState.readLine( this, s );
    }

    public static void main( String[] args ) {
        Class<?> clazz = ByteCodeReader.class;
        if ( args.length > 0 ) {
            try {
                clazz = Class.forName( args[ 0 ] );
            } catch ( ClassNotFoundException ex ) {
                Logger.getLogger( ByteCodeReader.class.getName() ).log( Level.SEVERE, null, ex );
            }
        }

        ByteCodeReader readCode = new ByteCodeReader( clazz );

        readCode.methodMap.entrySet().forEach(
                ( me ) -> {
                    System.out.println( "" + me.getKey() + '{' );
                    me.getValue().forEach( System.out::println );
                    System.out.println( "}\n" );
                }
        );

        readCode.codeSizeMap.entrySet()
                .stream()
                .forEach( entry -> {
                    System.out.println( entry.getKey() + " byte code size = " + entry.getValue() );
                } );

        readCode.federatedClassFiles.stream().forEach(
                clz -> {
                    System.out.println( "federate clz file Name = " + clz );
                }
        );

        System.out.println( "total byte code size of class '" + clazz.getName()
                + "' = " + readCode.classByteCodeTotalSize + " bytes" );
    }

    @Override
    public String toString() {
        return "ByteCodeReader{" + "clazz=" + clazzBaseName + ", methodMap=" + methodMap + '}';
    }

    public Map<String, Integer> getCodeSizeMap() {
        return codeSizeMap;
    }

    public int getClassByteCodeTotalSize() {
        return classByteCodeTotalSize.get();
    }

    private static final Map<String, ByteCodeReader> readerCache = new HashMap<>();

    public static Map<String, ByteCodeReader> getReaderCache() {
        return Collections.unmodifiableMap( readerCache );
    }

    static String classToFileNameBase( Class<?> clz ) {
        return clz.getName().replaceAll( "\\.", "/" );
    }

    static String classToFileName( Class<?> clz ) {
        return classToFileNameBase( clz ) + ".class";
    }
}
