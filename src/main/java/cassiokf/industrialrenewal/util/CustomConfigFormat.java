package cassiokf.industrialrenewal.util;

import com.electronwill.nightconfig.core.Config;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;
import java.util.function.Supplier;

public class CustomConfigFormat
{
    public static class ConfigValue<T>
    {
        private final ForgeConfigSpec.Builder parent;
        private final List<String> path;
        private final Supplier<T> defaultSupplier;

        private ForgeConfigSpec spec;

        ConfigValue(ForgeConfigSpec.Builder parent, List<String> path, Supplier<T> defaultSupplier)
        {
            this.parent = parent;
            this.path = path;
            this.defaultSupplier = defaultSupplier;
            this.parent.values.add(this);
        }

        public List<String> getPath()
        {
            return Lists.newArrayList(path);
        }

        public T get()
        {
            Preconditions.checkNotNull(spec, "Cannot get config value before spec is built");
            if (spec.childConfig == null)
                return defaultSupplier.get();
            return getRaw(spec.childConfig, path, defaultSupplier);
        }

        protected T getRaw(Config config, List<String> path, Supplier<T> defaultSupplier)
        {
            return config.getOrElse(path, defaultSupplier);
        }

        public ForgeConfigSpec.Builder next()
        {
            return parent;
        }

        public void save()
        {
            Preconditions.checkNotNull(spec, "Cannot save config value before spec is built");
            Preconditions.checkNotNull(spec.childConfig, "Cannot save config value without assigned Config object present");
            spec.save();
        }

        public void set(T value)
        {
            Preconditions.checkNotNull(spec, "Cannot set config value before spec is built");
            Preconditions.checkNotNull(spec.childConfig, "Cannot set config value without assigned Config object present");
            spec.childConfig.set(path, value);
        }
    }

    public static class FloatValue extends ForgeConfigSpec.ConfigValue<Float>
    {
        FloatValue(ForgeConfigSpec.Builder parent, List<String> path, Supplier<Float> defaultSupplier)
        {
            super(parent, path, defaultSupplier);
        }

        @Override
        protected Float getRaw(Config config, List<String> path, Supplier<Float> defaultSupplier)
        {
            Number n = config.get(path);
            return n == null ? defaultSupplier.get() : n.floatValue();
        }
    }
}
