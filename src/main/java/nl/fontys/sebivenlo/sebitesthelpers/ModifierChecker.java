package nl.fontys.sebivenlo.sebitesthelpers;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import static java.lang.reflect.Modifier.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 *
 * @author Pieter van den Hombergh {@code p.vandehombergh@fontysvenlo.org}
 */
public class ModifierChecker implements Predicate<Member> {

    final int mask;
    final int required;

    public ModifierChecker( int mask, int required ) {
        this.mask = mask;
        this.required = required;
    }

    public void check( Member m ) {
        String simpleName = m.getClass().getSimpleName();
        String clsName = m.getDeclaringClass().getCanonicalName();
        if (!test(m)) {
        throw new AssertionError(simpleName +" '"+clsName
                + "."+m.getName() + "' does not have the required modifiers [" + Modifier.toString( required ) + "]");
        }
    }

    @Override
    public boolean test( Member member ) {
        return ( member.getModifiers() & mask ) == required;
    }

    public static final ModifierChecker REQUIRE_PRIVATE
            = new ModifierChecker( NFTestHelpers.ALL_VISIBILITY_MASK, PRIVATE );
    public static final ModifierChecker REQUIRE_DEFAULT
            = new ModifierChecker( NFTestHelpers.ALL_VISIBILITY_MASK, 0 );

    public static final ModifierChecker REQUIRE_FINAL
            = new ModifierChecker( NFTestHelpers.ALL_VISIBILITY_MASK, FINAL);

    public static final ModifierChecker REQUIRE_PRIVATE_FINAL
            = new ModifierChecker( NFTestHelpers.ALL_VISIBILITY_MASK | FINAL, PRIVATE | FINAL );

    public static final ModifierChecker REQUIRE_DEFAULT_FINAL
            = new ModifierChecker( NFTestHelpers.ALL_VISIBILITY_MASK | FINAL, FINAL );

    public static final ModifierChecker REQUIRE_PRIVATE_STATIC
            = new ModifierChecker( NFTestHelpers.ALL_VISIBILITY_MASK | STATIC, PRIVATE | STATIC );

    public static final ModifierChecker REQUIRE_PUBLIC
            = new ModifierChecker( NFTestHelpers.ALL_VISIBILITY_MASK | STATIC, PUBLIC );

    public static final ModifierChecker REQUIRE_PUBLIC_FINAL
            = new ModifierChecker( NFTestHelpers.ALL_VISIBILITY_MASK | STATIC, PUBLIC | FINAL );

    public static final ModifierChecker REQUIRE_PUBLIC_STATIC
            = new ModifierChecker( NFTestHelpers.ALL_VISIBILITY_MASK | STATIC, PUBLIC | STATIC );

    public static final ModifierChecker REQUIRE_PUBLIC_STATIC_FINAL
            = new ModifierChecker( NFTestHelpers.ALL_VISIBILITY_MASK | STATIC, PUBLIC | STATIC | FINAL );

    
    /**
     * Check that the fields of field-types that are keys in the map have the
     * modifier (e.g. PRIVATE) as specified as value in the map.
     *
     * @param modMap map of class to modifier checker.
     * @param declaredFields the fields to be checked.
     */
    public static void checkModifiersOnFields( Map<Class<?>, ModifierChecker> modMap, Field[] declaredFields ) {
        checkModifiersOnFields( modMap, Arrays.asList( declaredFields ) );
    }

    /**
     * Check that the fields of field-types that are keys in the map have the
     * modifier (e.g. PRIVATE) as specified as value in the map.
     *
     * @param modMap map of class to modifier checker.
     * @param declaredFields the fields to be checked.
     */
    public static void checkModifiersOnFields( Map<Class<?>, ModifierChecker> modMap, List<Field> declaredFields ) {
        declaredFields.stream()
                .filter( fld -> modMap.containsKey( fld.getType() ) )
                .forEach( f -> modMap.get( f.getType() ).check( f ) );
    }
}
