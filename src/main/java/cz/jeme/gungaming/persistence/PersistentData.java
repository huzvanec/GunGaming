package cz.jeme.gungaming.persistence;

import io.papermc.paper.persistence.PersistentDataContainerView;
import io.papermc.paper.persistence.PersistentDataViewHolder;
import net.kyori.adventure.key.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

@NullMarked
public interface PersistentData<P, C> extends Keyed {
    // static creators

    static <P, C> PersistentData<P, C> of(final NamespacedKey key, final PersistentDataType<P, C> type) {
        return new PersistentDataImpl<>(key, type);
    }

    static PersistentData<Byte, Byte> ofByte(final NamespacedKey key) {
        return PersistentData.of(key, PersistentDataType.BYTE);
    }

    static PersistentData<Short, Short> ofShort(final NamespacedKey key) {
        return PersistentData.of(key, PersistentDataType.SHORT);
    }

    static PersistentData<Integer, Integer> ofInteger(final NamespacedKey key) {
        return PersistentData.of(key, PersistentDataType.INTEGER);
    }

    static PersistentData<Long, Long> ofLong(final NamespacedKey key) {
        return PersistentData.of(key, PersistentDataType.LONG);
    }

    static PersistentData<Float, Float> ofFloat(final NamespacedKey key) {
        return PersistentData.of(key, PersistentDataType.FLOAT);
    }

    static PersistentData<Double, Double> ofDouble(final NamespacedKey key) {
        return PersistentData.of(key, PersistentDataType.DOUBLE);
    }

    static PersistentData<Byte, Boolean> ofBoolean(final NamespacedKey key) {
        return PersistentData.of(key, PersistentDataType.BOOLEAN);
    }

    static PersistentData<String, String> ofString(final NamespacedKey key) {
        return PersistentData.of(key, PersistentDataType.STRING);
    }

    static PersistentData<byte[], byte[]> ofByteArray(final NamespacedKey key) {
        return PersistentData.of(key, PersistentDataType.BYTE_ARRAY);
    }

    static PersistentData<int[], int[]> ofIntegerArray(final NamespacedKey key) {
        return PersistentData.of(key, PersistentDataType.INTEGER_ARRAY);
    }

    static PersistentData<long[], long[]> ofLongArray(final NamespacedKey key) {
        return PersistentData.of(key, PersistentDataType.LONG_ARRAY);
    }

    static PersistentData<PersistentDataContainer, PersistentDataContainer> ofDataContainer(final NamespacedKey key) {
        return PersistentData.of(key, PersistentDataType.TAG_CONTAINER);
    }

    // this should never be used with an existing type!
    static <P, C> PersistentData<List<P>, List<C>> ofList(final NamespacedKey key, final PersistentDataType<P, C> type) {
        return PersistentData.of(key, PersistentDataType.LIST.listTypeFrom(type));
    }

    static PersistentData<List<Byte>, List<Byte>> ofByteList(final NamespacedKey key) {
        return PersistentData.of(key, PersistentDataType.LIST.bytes());
    }

    static PersistentData<List<Short>, List<Short>> ofShortList(final NamespacedKey key) {
        return PersistentData.of(key, PersistentDataType.LIST.shorts());
    }

    static PersistentData<List<Integer>, List<Integer>> ofIntegerList(final NamespacedKey key) {
        return PersistentData.of(key, PersistentDataType.LIST.integers());
    }

    static PersistentData<List<Long>, List<Long>> ofLongList(final NamespacedKey key) {
        return PersistentData.of(key, PersistentDataType.LIST.longs());
    }

    static PersistentData<List<Float>, List<Float>> ofFloatList(final NamespacedKey key) {
        return PersistentData.of(key, PersistentDataType.LIST.floats());
    }

    static PersistentData<List<Double>, List<Double>> ofDoubleList(final NamespacedKey key) {
        return PersistentData.of(key, PersistentDataType.LIST.doubles());
    }

    static PersistentData<List<Byte>, List<Boolean>> ofBooleanList(final NamespacedKey key) {
        return PersistentData.of(key, PersistentDataType.LIST.booleans());
    }

    static PersistentData<List<String>, List<String>> ofStringList(final NamespacedKey key) {
        return PersistentData.of(key, PersistentDataType.LIST.strings());
    }

    static PersistentData<List<byte[]>, List<byte[]>> ofByteArrayList(final NamespacedKey key) {
        return PersistentData.of(key, PersistentDataType.LIST.byteArrays());
    }

    static PersistentData<List<int[]>, List<int[]>> ofIntegerArrayList(final NamespacedKey key) {
        return PersistentData.of(key, PersistentDataType.LIST.integerArrays());
    }

    static PersistentData<List<long[]>, List<long[]>> ofLongArrayList(final NamespacedKey key) {
        return PersistentData.of(key, PersistentDataType.LIST.longArrays());
    }

    static PersistentData<List<PersistentDataContainer>, List<PersistentDataContainer>> ofDataContainerList(final NamespacedKey key) {
        return PersistentData.of(key, PersistentDataType.LIST.dataContainers());
    }

    // body


    @Override
    NamespacedKey key();

    PersistentDataType<P, C> type();

    void write(final PersistentDataContainer container, final C value);

    default void write(final PersistentDataHolder holder, final C value) {
        write(holder.getPersistentDataContainer(), value);
    }

    default void write(final ItemStack item, final C value) {
        item.editMeta(meta -> write(meta, value));
    }


    Optional<C> read(final @Nullable PersistentDataContainerView container);


    default Optional<C> read(final @Nullable PersistentDataViewHolder holder) {
        if (holder == null) return Optional.empty();
        return read(holder.getPersistentDataContainer());
    }

    C require(final PersistentDataContainerView container);

    default C require(final PersistentDataViewHolder holder) {
        return require(holder.getPersistentDataContainer());
    }

    boolean check(final @Nullable PersistentDataContainerView container);

    default boolean check(final @Nullable PersistentDataHolder holder) {
        return holder != null && check(holder.getPersistentDataContainer());
    }

    void delete(final PersistentDataContainer container);

    default void delete(final PersistentDataHolder holder) {
        delete(holder.getPersistentDataContainer());
    }

    default void delete(final ItemStack item) {
        item.editMeta(this::delete);
    }
}
