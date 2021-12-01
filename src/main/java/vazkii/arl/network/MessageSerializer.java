package vazkii.arl.network;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.network.chat.Component;

@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
public final class MessageSerializer {

	private static final HashMap<Class<?>, Pair<Reader, Writer>> handlers = new HashMap<>();
	private static final HashMap<Class<?>, Field[]> fieldCache = new HashMap<>();

	static {
		MessageSerializer.<Byte> mapHandler(byte.class, FriendlyByteBuf::readByte, FriendlyByteBuf::writeByte);
		MessageSerializer.<Short> mapHandler(short.class, FriendlyByteBuf::readShort, FriendlyByteBuf::writeShort);
		MessageSerializer.<Integer> mapHandler(int.class, FriendlyByteBuf::readInt, FriendlyByteBuf::writeInt);
		MessageSerializer.<Long> mapHandler(long.class, FriendlyByteBuf::readLong, FriendlyByteBuf::writeLong);
		MessageSerializer.<Float> mapHandler(float.class, FriendlyByteBuf::readFloat, FriendlyByteBuf::writeFloat);
		MessageSerializer.<Double> mapHandler(double.class, FriendlyByteBuf::readDouble, FriendlyByteBuf::writeDouble);
		MessageSerializer.<Boolean> mapHandler(boolean.class, FriendlyByteBuf::readBoolean, FriendlyByteBuf::writeBoolean);
		MessageSerializer.<Character> mapHandler(char.class, FriendlyByteBuf::readChar, FriendlyByteBuf::writeChar);

		mapHandler(BlockPos.class, FriendlyByteBuf::readBlockPos, FriendlyByteBuf::writeBlockPos);
		mapHandler(Component.class, FriendlyByteBuf::readComponent, FriendlyByteBuf::writeComponent);
		mapHandler(UUID.class, FriendlyByteBuf::readUUID, FriendlyByteBuf::writeUUID);
		mapHandler(CompoundTag.class, FriendlyByteBuf::readNbt, FriendlyByteBuf::writeNbt);
		mapHandler(ItemStack.class, FriendlyByteBuf::readItem, MessageSerializer::writeItemStack);
		mapHandler(String.class, MessageSerializer::readString, MessageSerializer::writeString);
		mapHandler(ResourceLocation.class, FriendlyByteBuf::readResourceLocation, FriendlyByteBuf::writeResourceLocation);
		mapHandler(Date.class, FriendlyByteBuf::readDate, FriendlyByteBuf::writeDate);
		mapHandler(BlockHitResult.class, FriendlyByteBuf::readBlockHitResult, FriendlyByteBuf::writeBlockHitResult);
	}
	
	public static void readObject(Object obj, FriendlyByteBuf buf) {
		try {
			Class<?> clazz = obj.getClass();
			Field[] clFields = getClassFields(clazz);
			for(Field f : clFields) {
				Class<?> type = f.getType();
				if(acceptField(f, type))
					readField(obj, f, type, buf);
			}
		} catch(Exception e) {
			throw new RuntimeException("Error at reading message " + obj, e);
		}
	}
	
	public static void writeObject(Object obj, FriendlyByteBuf buf) {
		try {
			Class<?> clazz = obj.getClass();
			Field[] clFields = getClassFields(clazz);
			for(Field f : clFields) {
				Class<?> type = f.getType();
				if(acceptField(f, type))
					writeField(obj, f, type, buf);
			}
		} catch(Exception e) {
			throw new RuntimeException("Error at writing message " + obj, e);
		}
	}

	private static Field[] getClassFields(Class<?> clazz) {
		if(fieldCache.containsKey(clazz))
			return fieldCache.get(clazz);
		else {
			Field[] fields = clazz.getFields();
			Arrays.sort(fields, Comparator.comparing(Field::getName));
			fieldCache.put(clazz, fields);
			return fields;
		}
	}

	private static void writeField(Object obj, Field f, Class<?> clazz, FriendlyByteBuf buf) throws IllegalArgumentException, IllegalAccessException {
		Pair<Reader, Writer> handler = getHandler(clazz);
		handler.getRight().write(buf, f, f.get(obj));
	}

	private static void readField(Object obj, Field f, Class<?> clazz, FriendlyByteBuf buf) throws IllegalArgumentException, IllegalAccessException {
		Pair<Reader, Writer> handler = getHandler(clazz);
		f.set(obj, handler.getLeft().read(buf, f));
	}

	private static Pair<Reader, Writer> getHandler(Class<?> clazz) {
		Pair<Reader, Writer> pair = handlers.get(clazz);
		if(pair == null)
			throw new RuntimeException("No R/W handler for  " + clazz);
		return pair;
	}

	private static boolean acceptField(Field f, Class<?> type) {
		int mods = f.getModifiers();
		if(Modifier.isFinal(mods) || Modifier.isStatic(mods) || Modifier.isTransient(mods))
			return false;

		return  handlers.containsKey(type);
	}

	private static <T> void mapHandler(Class<T> type, Function<FriendlyByteBuf, T> readerLower, BiConsumer<FriendlyByteBuf, T> writerLower) {
		Reader<T> reader = (buf, field) -> readerLower.apply(buf);
		Writer<T> writer = (buf, field, t) -> writerLower.accept(buf, t);
		mapHandler(type, reader, writer);
	}

	private static <T> void mapHandler(Class<T> type, Reader<T> reader, BiConsumer<FriendlyByteBuf, T> writerLower) {
		Writer<T> writer = (buf, field, t) -> writerLower.accept(buf, t);
		mapHandler(type, reader, writer);	
	}

	private static <T> void mapHandler(Class<T> type, Function<FriendlyByteBuf, T> readerLower, Writer<T> writer) {
		Reader<T> reader = (buf, field) -> readerLower.apply(buf);
		mapHandler(type, reader, writer);
	}

	public static <T> void mapHandler(Class<T> type, Reader<T> reader, Writer<T> writer) {
		Class<T[]> arrayType = (Class<T[]>) Array.newInstance(type, 0).getClass();

		Reader<T[]> arrayReader = (buf, field) -> {
			int count = buf.readInt();
			T[] arr = (T[]) Array.newInstance(type, count);

			for(int i = 0; i < count; i++)
				arr[i] = reader.read(buf, field);

			return arr;
		};
		
		Writer<T[]> arrayWriter = (buf, field, t) -> {
			int count = t.length;
			buf.writeInt(count);
			
			for(int i = 0; i < count; i++)
				writer.write(buf, field, t[i]);
		};
		
		handlers.put(type, Pair.of(reader, writer));
		handlers.put(arrayType, Pair.of(arrayReader, arrayWriter));
	}

	// ================================================================
	// Auxiliary I/O
	// ================================================================

	// Needed because the methods are overloaded

	private static void writeItemStack(FriendlyByteBuf buf, ItemStack stack) {
		buf.writeItem(stack);
	}

	private static String readString(FriendlyByteBuf buf) {
		return buf.readUtf(32767);
	}

	private static void writeString(FriendlyByteBuf buf, String string) {
		buf.writeUtf(string);
	}

	// ================================================================
	// Functional interfaces
	// ================================================================

	public static interface Reader<T> {
		public T read(FriendlyByteBuf buf, Field field);
	}

	public static interface Writer<T> {
		public void write(FriendlyByteBuf buf, Field field, T t);
	}

}
