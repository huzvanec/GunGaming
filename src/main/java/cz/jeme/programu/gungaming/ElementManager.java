package cz.jeme.programu.gungaming;

import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.item.ammo.Ammo;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public enum ElementManager {
    INSTANCE;

    private final @NotNull Map<Class<? extends CustomElement>, CustomElement> classified = new HashMap<>();
    private final @NotNull Map<String, CustomElement> keyed = new HashMap<>();
    private final @NotNull Map<String, Set<CustomItem>> tagged = new HashMap<>();

    private static final @NotNull List<Class<? extends CustomElement>> ORDER = List.of(
            Ammo.class, // load ammo before all other items (guns need it to register)
            CustomItem.class,
            CustomElement.class
    );

    private static int getOrder(final @NotNull Class<? extends CustomElement> clazz) {
        for (int i = 0; i < ORDER.size(); i++) {
            if (ORDER.get(i).isAssignableFrom(clazz)) return i;
        }
        throw new RuntimeException(clazz.getName() + " was not found in order list!");
    }

    public void registerElements(final String @NotNull ... packageNames) {
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
        final long duration = System.currentTimeMillis() - start;
        GunGaming.logger().info("Elements registered successfully. (took %s ms)".formatted(duration));
    }

    private void construct(final @NotNull List<Set<Class<? extends CustomElement>>> elements) {
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
                            tagged.putIfAbsent(tag, new HashSet<>());
                            tagged.get(tag).add(customItem);
                        }
                        customItem.init();
                    }
                } catch (final NoSuchMethodException e) {
                    throw new RuntimeException("CustomElement class \"" + elementClass.getCanonicalName() + "\" is missing an empty constructor!", e);
                } catch (final InvocationTargetException e) {
                    throw new RuntimeException("Could not create new instance of \"" + elementClass.getCanonicalName() + "\"", e);
                } catch (final InstantiationException e) {
                    // this should never happen
                    throw new RuntimeException("Could not instantiate \"" + elementClass.getCanonicalName() + "\"", e);
                } catch (final IllegalAccessException e) {
                    // this should never happen
                    throw new RuntimeException("Could not access constructor of class \"" + elementClass.getCanonicalName() + "\"", e);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends CustomElement> @NotNull Optional<T> getElement(final @NotNull Class<T> elementClass) {
        return (Optional<T>) Optional.ofNullable(classified.get(elementClass));
    }

    public @NotNull Optional<? extends CustomElement> getElement(final @NotNull String keyStr) {
        return Optional.ofNullable(keyed.get(keyStr));
    }

    public <T extends CustomElement> @NotNull Optional<T> getElement(final @NotNull String keyStr, final @NotNull Class<T> elementClass) {
        final CustomElement element = keyed.get(keyStr);
        if (element == null) return Optional.empty();
        if (!elementClass.isInstance(element)) return Optional.empty();
        @SuppressWarnings("unchecked") final T tElement = (T) element;
        return Optional.of(tElement);
    }

    public boolean existsElement(final @NotNull Class<? extends CustomElement> elementClass) {
        return classified.containsKey(elementClass);
    }

    public boolean existsElement(final @NotNull String keyStr) {
        return keyed.containsKey(keyStr);
    }

    public boolean existsElement(final @NotNull String keyStr, final @NotNull Class<? extends CustomElement> elementClass) {
        final CustomElement element = keyed.get(keyStr);
        if (element == null) return false;
        return elementClass.isInstance(element);
    }

    public boolean existsTag(final @NotNull String tag) {
        return tagged.containsKey(tag);
    }

    public @NotNull Set<CustomItem> getItems(final @NotNull String tag) {
        return existsTag(tag)
                ? new HashSet<>(tagged.get(tag))
                : Set.of();
    }

    public @NotNull Set<String> keys() {
        return new HashSet<>(keyed.keySet());
    }

    public @NotNull Set<CustomElement> elements() {
        return new HashSet<>(classified.values());
    }

    public @NotNull Set<String> tags() {
        return new HashSet<>(tagged.keySet());
    }
}