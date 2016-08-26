package swen221.lab9.simplyfyReflection2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionHelper {
	public static interface SupplierWithException<T> {
		/* FIXME you may want to change code here */

		T get() throws Throwable;
	}

	public static <T> T tryCatch(SupplierWithException<T> s) {
		/* FIXME add here the try-catching logic as from the text */
		try {
			return s.get();
		}

		catch (IllegalAccessException e) {
			throw new Error("", e);
		} catch (NoSuchMethodException e) {
			throw new Error("", e);
		} catch (SecurityException e) {
			throw new Error("ERROR: ", e);
		}

		catch (InvocationTargetException e) {
			Throwable thr = e.getCause();
			if (thr instanceof RuntimeException)
				throw (RuntimeException) thr;
			if (thr instanceof Error)
				throw (Error) thr;
			throw new Error("", thr);

		} catch (Throwable e) {
			throw new Error("", e);

		}
	}
}
