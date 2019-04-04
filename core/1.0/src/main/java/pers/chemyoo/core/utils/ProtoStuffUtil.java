package pers.chemyoo.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import lombok.extern.slf4j.Slf4j;
import pers.chemyoo.core.setting.exception.CustomException;

@Slf4j
public class ProtoStuffUtil {

	private static final String ERROR_LOG_MESSAGE = "Failed to serializer";

	private static ThreadLocal<LinkedBuffer> localLinkBuffer = new ThreadLocal<>();

	private static LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);

	private ProtoStuffUtil() {
	}
	
	/**
	 * 相同线程从ThreadLocal可以拿值，当前线程结束后ThreadLocal值就为空了。
	 * @return
	 */
	private static LinkedBuffer getLocalLinkBuffer() {
		if(localLinkBuffer.get() == null) {
			localLinkBuffer.set(buffer);
		}
		return localLinkBuffer.get();
	}

	/**
	 * 序列化对象
	 *
	 * @param obj
	 * @return
	 */
	public static <T> byte[] serialize(T obj) {
		if (obj == null) {
			log.error("Failed to serializer, obj is null");
			throw new CustomException("500", ERROR_LOG_MESSAGE);
		}

		@SuppressWarnings("unchecked")
		Schema<T> schema = (Schema<T>) RuntimeSchema.getSchema(obj.getClass());
		LinkedBuffer buffer = getLocalLinkBuffer();
		byte[] protoStuff;
		try {
			protoStuff = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
		} catch (Exception e) {
			log.error("Failed to serializer, obj:{}", obj, e);
			throw new CustomException("500", ERROR_LOG_MESSAGE);
		} finally {
			buffer.clear();
		}
		return protoStuff;
	}

	/**
	 * 反序列化对象
	 *
	 * @param paramArrayOfByte
	 * @param targetClass
	 * @return
	 */
	public static <T> T deserialize(byte[] paramArrayOfByte, Class<T> targetClass) {
		if (paramArrayOfByte == null || paramArrayOfByte.length == 0) {
			log.error("Failed to deserialize, byte is empty");
			throw new CustomException("500", ERROR_LOG_MESSAGE);
		}

		T instance;
		try {
			instance = targetClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			log.error("Failed to deserialize", e);
			throw new CustomException("500", ERROR_LOG_MESSAGE);
		}

		Schema<T> schema = RuntimeSchema.getSchema(targetClass);
		ProtostuffIOUtil.mergeFrom(paramArrayOfByte, instance, schema);
		return instance;
	}

	/**
	 * 序列化列表
	 *
	 * @param objList
	 * @return
	 */
	public static <T> byte[] serializeList(List<T> objList) {
		if (objList == null || objList.isEmpty()) {
			log.error("Failed to serializer, objList is empty");
			throw new CustomException("500", ERROR_LOG_MESSAGE);
		}

		@SuppressWarnings("unchecked")
		Schema<T> schema = (Schema<T>) RuntimeSchema.getSchema(objList.get(0).getClass());
		LinkedBuffer buffer = getLocalLinkBuffer();
		byte[] protoStuff;
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			ProtostuffIOUtil.writeListTo(bos, objList, schema, buffer);
			protoStuff = bos.toByteArray();
		} catch (Exception e) {
			log.error("Failed to serializer, obj list:{}", objList, e);
			throw new CustomException("500", ERROR_LOG_MESSAGE);
		} finally {
			buffer.clear();
		}

		return protoStuff;
	}

	/**
	 * 反序列化列表
	 *
	 * @param paramArrayOfByte
	 * @param targetClass
	 * @return
	 */
	public static <T> List<T> deserializeList(byte[] paramArrayOfByte, Class<T> targetClass) {
		if (paramArrayOfByte == null || paramArrayOfByte.length == 0) {
			log.error("Failed to deserialize, byte is empty");
			throw new CustomException("500", ERROR_LOG_MESSAGE);
		}

		Schema<T> schema = RuntimeSchema.getSchema(targetClass);
		List<T> result;
		try {
			result = ProtostuffIOUtil.parseListFrom(new ByteArrayInputStream(paramArrayOfByte), schema);
		} catch (IOException e) {
			log.error("Failed to deserialize", e);
			throw new CustomException("500", ERROR_LOG_MESSAGE);
		}
		return result;
	}

}
