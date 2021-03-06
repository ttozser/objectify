package com.googlecode.objectify.impl.translate.opt.joda;

import com.googlecode.objectify.impl.Path;
import com.googlecode.objectify.impl.TypeUtils;
import com.googlecode.objectify.impl.translate.CreateContext;
import com.googlecode.objectify.impl.translate.LoadContext;
import com.googlecode.objectify.impl.translate.SaveContext;
import com.googlecode.objectify.impl.translate.SkipException;
import com.googlecode.objectify.impl.translate.TypeKey;
import com.googlecode.objectify.impl.translate.ValueTranslator;
import com.googlecode.objectify.impl.translate.ValueTranslatorFactory;
import org.joda.time.ReadableInstant;

import java.lang.reflect.Constructor;
import java.util.Date;


/**
 * Converts Joda ReadableInstants (DateTime, DateMidnight, etc) into java.util.Date 
 * 
 * @author Jeff Schnitzer <jeff@infohazard.org>
 */
public class ReadableInstantTranslatorFactory extends ValueTranslatorFactory<ReadableInstant, Date>
{
	public ReadableInstantTranslatorFactory() {
		super(ReadableInstant.class);
	}

	@Override
	protected ValueTranslator<ReadableInstant, Date> createValueTranslator(TypeKey<ReadableInstant> tk, CreateContext ctx, Path path) {
		final Class<?> clazz = tk.getTypeAsClass();

		return new ValueTranslator<ReadableInstant, Date>(Date.class) {
			@Override
			protected ReadableInstant loadValue(Date value, LoadContext ctx, Path path) throws SkipException {
				// All the Joda instants have a constructor that will take a Date
				Constructor<?> ctor = TypeUtils.getConstructor(clazz, Object.class);
				return (ReadableInstant)TypeUtils.newInstance(ctor, value);
			}

			@Override
			protected Date saveValue(ReadableInstant value, boolean index, SaveContext ctx, Path path) throws SkipException {
				return value.toInstant().toDate();
			}
		};
	}
}