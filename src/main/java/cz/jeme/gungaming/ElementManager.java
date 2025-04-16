package cz.jeme.gungaming;

import cz.jeme.gungaming.item.CustomItem;
import cz.jeme.gungaming.item.ammo.Ammo;
import cz.jeme.gungaming.item.armor.impl.StealthHelmet;
import cz.jeme.gungaming.item.throwable.impl.RocketThrowable;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import org.jspecify.annotations.NullMarked;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

@NullMarked
public enum ElementManager {
    INSTANCE;

    private final Map<Class<? extends CustomElement>, CustomElement> classified = new HashMap<>();
    private final Map<String, CustomElement> keyed = new HashMap<>();
    private final Map<String, Set<CustomItem>> tagged = new HashMap<>();

    private static final List<Class<? extends CustomElement>> ORDER = List.of(
            Ammo.class, // load all ammo before guns
            StealthHelmet.class, // load stealth helmet before radar and player trackers
            RocketThrowable.class, // load rocket throwable before rocket launcher
            CustomItem.class,
            CustomElement.class
    );

    private static int getOrder(final Class<? extends CustomElement> clazz) {
        for (int i = 0; i < ORDER.size(); i++) {
            if (ORDER.get(i).isAssignableFrom(clazz)) return i;
        }
        throw new RuntimeException(clazz.getName() + " was not found in order list!");
    }

    public void registerElements(final String... packageNames) {
        final long start = System.currentTimeMillis();

        GunGaming.logger().info("Registering elements: " + Arrays.toString(packageNames));

        try (final ScanResult result = new ClassGraph()
                .acceptPackages(packageNames)
                .scan()) {
            final ClassInfoList classes = result.getSubclasses(CustomElement.class);
            final List<Set<Class<? extends CustomElement>>> elements = new ArrayList<>();
            for (int i = 0; i < ORDER.size(); i++) elements.add(new HashSet<>());
            for (final ClassInfo info : classes) {
                if (info.isAbstract()) continue;
                @SuppressWarnings("unchecked") final Class<? extends CustomElement> elementClass = (Class<? extends CustomElement>) info.loadClass();
                elements.get(getOrder(elementClass)).add(elementClass);
            }
            construct(elements);
        }

        GunGaming.logger().info("Elements registered successfully (took %sms)".formatted(
                System.currentTimeMillis() - start
        ));
    }

    private void construct(final List<Set<Class<? extends CustomElement>>> elements) {
        for (final Set<Class<? extends CustomElement>> elementSet : elements) {
            for (final Class<? extends CustomElement> elementClass : elementSet) {
                try {
                    final Constructor<? extends CustomElement> constructor = elementClass.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    final CustomElement element = constructor.newInstance();
                    classified.put(elementClass, element);
                    keyed.put(element.key().asString(), element);
                    if (element instanceof final CustomItem customItem) {
                        for (final String tag : customItem.tags()) {
                            tagged.computeIfAbsent(
                                    tag,
                                    k -> new HashSet<>()
                            ).add(customItem);
                        }
                        customItem.init();
                    }
                } catch (final NoSuchMethodException e) {
                    throw new RuntimeException("CustomElement class \"" + elementClass.getCanonicalName() + "\" is missing an empty constructor!", e);
                } catch (final InvocationTargetException e) {
                    throw new RuntimeException("Could not create new instance of \"" + elementClass.getCanonicalName() + "\"", e);
                } catch (final InstantiationException e) {
                    // this should never happen
                    throw new AssertionError("Could not instantiate \"" + elementClass.getCanonicalName() + "\"", e);
                } catch (final IllegalAccessException e) {
                    // this should never happen
                    throw new AssertionError("Could not access constructor of class \"" + elementClass.getCanonicalName() + "\"", e);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends CustomElement> Optional<T> getElement(final Class<T> elementClass) {
        return (Optional<T>) Optional.ofNullable(classified.get(elementClass));
    }

    public Optional<? extends CustomElement> getElement(final String keyStr) {
        return Optional.ofNullable(keyed.get(keyStr));
    }

    public <T extends CustomElement> Optional<T> getElement(final String keyStr, final Class<T> elementClass) {
        final CustomElement element = keyed.get(keyStr);
        if (element == null) return Optional.empty();
        if (!elementClass.isInstance(element)) return Optional.empty();
        @SuppressWarnings("unchecked") final T tElement = (T) element;
        return Optional.of(tElement);
    }

    public boolean existsElement(final Class<? extends CustomElement> elementClass) {
        return classified.containsKey(elementClass);
    }

    public boolean existsElement(final String keyStr) {
        return keyed.containsKey(keyStr);
    }

    public boolean existsElement(final String keyStr, final Class<? extends CustomElement> elementClass) {
        final CustomElement element = keyed.get(keyStr);
        if (element == null) return false;
        return elementClass.isInstance(element);
    }

    public boolean existsTag(final String tag) {
        return tagged.containsKey(tag);
    }

    public Set<CustomItem> getItems(final String tag) {
        return existsTag(tag)
                ? new HashSet<>(tagged.get(tag))
                : Set.of();
    }

    public Set<String> keys() {
        return new HashSet<>(keyed.keySet());
    }

    public Set<CustomElement> elements() {
        return new HashSet<>(classified.values());
    }

    public Set<CustomItem> items() {
        return classified.values().stream()
                .filter(CustomItem.class::isInstance)
                .map(CustomItem.class::cast)
                .collect(Collectors.toUnmodifiableSet());
    }

    public Set<String> tags() {
        return new HashSet<>(tagged.keySet());
    }
}