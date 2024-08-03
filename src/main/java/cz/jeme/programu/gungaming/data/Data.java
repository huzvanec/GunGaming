package cz.jeme.programu.gungaming.data;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public interface Data<P, C> {
    // static creators

    static <P, C> @NotNull Data<P, C> of(final @NotNull NamespacedKey key, final @NotNull PersistentDataType<P, C> type) {
        return new DataImpl<>(key, type);
    }

    static @NotNull Data<Byte, Byte> ofByte(final @NotNull NamespacedKey key) {
        return Data.of(key, PersistentDataType.BYTE);
    }

    static @NotNull Data<Short, Short> ofShort(final @NotNull NamespacedKey key) {
        return Data.of(key, PersistentDataType.SHORT);
    }

    static @NotNull Data<Integer, Integer> ofInteger(final @NotNull NamespacedKey key) {
        return Data.of(key, PersistentDataType.INTEGER);
    }

    static @NotNull Data<Long, Long> ofLong(final @NotNull NamespacedKey key) {
        return Data.of(key, PersistentDataType.LONG);
    }

    static @NotNull Data<Float, Float> ofFloat(final @NotNull NamespacedKey key) {
        return Data.of(key, PersistentDataType.FLOAT);
    }

    static @NotNull Data<Double, Double> ofDouble(final @NotNull NamespacedKey key) {
        return Data.of(key, PersistentDataType.DOUBLE);
    }

    static @NotNull Data<Byte, Boolean> ofBoolean(final @NotNull NamespacedKey key) {
        return Data.of(key, PersistentDataType.BOOLEAN);
    }

    static @NotNull Data<String, String> ofString(final @NotNull NamespacedKey key) {
        return Data.of(key, PersistentDataType.STRING);
    }

    static @NotNull Data<byte[], byte[]> ofByteArray(final @NotNull NamespacedKey key) {
        return Data.of(key, PersistentDataType.BYTE_ARRAY);
    }

    static @NotNull Data<int[], int[]> ofIntegerArray(final @NotNull NamespacedKey key) {
        return Data.of(key, PersistentDataType.INTEGER_ARRAY);
    }

    static @NotNull Data<long[], long[]> ofLongArray(final @NotNull NamespacedKey key) {
        return Data.of(key, PersistentDataType.LONG_ARRAY);
    }

    static @NotNull Data<PersistentDataContainer, PersistentDataContainer> ofDataContainer(final @NotNull NamespacedKey key) {
        return Data.of(key, PersistentDataType.TAG_CONTAINER);
    }

    // this should never be used with an existing type!
    static <P, C> @NotNull Data<List<P>, List<C>> ofList(final @NotNull NamespacedKey key, final @NotNull PersistentDataType<P, C> type) {
        return Data.of(key, PersistentDataType.LIST.listTypeFrom(type));
    }

    static @NotNull Data<List<Byte>, List<Byte>> ofByteList(final @NotNull NamespacedKey key) {
        return Data.of(key, PersistentDataType.LIST.bytes());
    }

    static @NotNull Data<List<Short>, List<Short>> ofShortList(final @NotNull NamespacedKey key) {
        return Data.of(key, PersistentDataType.LIST.shorts());
    }

    static @NotNull Data<List<Integer>, List<Integer>> ofIntegerList(final @NotNull NamespacedKey key) {
        return Data.of(key, PersistentDataType.LIST.integers());
    }

    static @NotNull Data<List<Long>, List<Long>> ofLongList(final @NotNull NamespacedKey key) {
        return Data.of(key, PersistentDataType.LIST.longs());
    }

    static @NotNull Data<List<Float>, List<Float>> ofFloatList(final @NotNull NamespacedKey key) {
        return Data.of(key, PersistentDataType.LIST.floats());
    }

    static @NotNull Data<List<Double>, List<Double>> ofDoubleList(final @NotNull NamespacedKey key) {
        return Data.of(key, PersistentDataType.LIST.doubles());
    }

    static @NotNull Data<List<Byte>, List<Boolean>> ofBooleanList(final @NotNull NamespacedKey key) {
        return Data.of(key, PersistentDataType.LIST.booleans());
    }

    static @NotNull Data<List<String>, List<String>> ofStringList(final @NotNull NamespacedKey key) {
        return Data.of(key, PersistentDataType.LIST.strings());
    }

    static @NotNull Data<List<byte[]>, List<byte[]>> ofByteArrayList(final @NotNull NamespacedKey key) {
        return Data.of(key, PersistentDataType.LIST.byteArrays());
    }

    static @NotNull Data<List<int[]>, List<int[]>> ofIntegerArrayList(final @NotNull NamespacedKey key) {
        return Data.of(key, PersistentDataType.LIST.integerArrays());
    }

    static @NotNull Data<List<long[]>, List<long[]>> ofLongArrayList(final @NotNull NamespacedKey key) {
        return Data.of(key, PersistentDataType.LIST.longArrays());
    }

    static @NotNull Data<List<PersistentDataContainer>, List<PersistentDataContainer>> ofDataContainerList(final @NotNull NamespacedKey key) {
        return Data.of(key, PersistentDataType.LIST.dataContainers());
    }

    // body

    @NotNull
    NamespacedKey key();

    @NotNull
    PersistentDataType<P, C> type();

    void write(final @NotNull PersistentDataContainer container, final @NotNull C value);

    void write(final @NotNull PersistentDataHolder holder, final @NotNull C value);

    void write(final @NotNull ItemStack item, final @NotNull C value);

    @NotNull
    Optional<C> read(final @Nullable PersistentDataContainer container);

    @NotNull
    Optional<C> read(final @Nullable PersistentDataHolder holder);

    @NotNull
    Optional<C> read(final @Nullable ItemStack item);

    @NotNull
    C require(final @NotNull PersistentDataContainer container);

    @NotNull
    C require(final @NotNull PersistentDataHolder holder);

    @NotNull
    C require(final @NotNull ItemStack item);

    boolean check(final @Nullable PersistentDataContainer container);

    boolean check(final @Nullable PersistentDataHolder holder);

    boolean check(final @Nullable ItemStack item);

    void delete(final @NotNull PersistentDataContainer container);

    void delete(final @NotNull PersistentDataHolder holder);

    void delete(final @NotNull ItemStack item);
}
