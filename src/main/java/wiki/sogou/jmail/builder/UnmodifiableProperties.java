package wiki.sogou.jmail.builder;

import java.io.*;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author JimYip
 */
public class UnmodifiableProperties extends Properties {
    private Properties properties;


    public UnmodifiableProperties() {
        super();
    }

    @Override
    public synchronized int size() {
        return properties.size();
    }

    @Override
    public synchronized boolean isEmpty() {
        return properties.isEmpty();
    }

    @Override
    public synchronized Enumeration<Object> keys() {
        return properties.keys();
    }

    @Override
    public synchronized Enumeration<Object> elements() {
        return properties.elements();
    }

    @Override
    public synchronized boolean contains(Object value) {
        return properties.contains(value);
    }

    @Override
    public boolean containsValue(Object value) {
        return properties.containsValue(value);
    }

    @Override
    public synchronized boolean containsKey(Object key) {
        return properties.containsKey(key);
    }

    @Override
    public synchronized Object get(Object key) {
        return properties.get(key);
    }

    @Override
    public synchronized Object put(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized Object remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized void putAll(Map<?, ?> t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized Object clone() {
        return properties.clone();
    }

    @Override
    public synchronized String toString() {
        return properties.toString();
    }

    @Override
    public Set<Object> keySet() {
        return properties.keySet();
    }

    @Override
    public Set<Map.Entry<Object, Object>> entrySet() {
        return properties.entrySet();
    }

    @Override
    public Collection<Object> values() {
        return properties.values();
    }

    @Override
    public synchronized boolean equals(Object o) {
        return properties.equals(o);
    }

    @Override
    public synchronized int hashCode() {
        return properties.hashCode();
    }

    @Override
    public synchronized Object getOrDefault(Object key, Object defaultValue) {
        return properties.getOrDefault(key, defaultValue);
    }

    @Override
    public synchronized void forEach(BiConsumer<? super Object, ? super Object> action) {
        properties.forEach(action);
    }

    @Override
    public synchronized void replaceAll(BiFunction<? super Object, ? super Object, ?> function) {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized Object putIfAbsent(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized boolean remove(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized boolean replace(Object key, Object oldValue, Object newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized Object replace(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized Object computeIfAbsent(Object key, Function<? super Object, ?> mappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized Object computeIfPresent(Object key, BiFunction<? super Object, ? super Object, ?> remappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized Object compute(Object key, BiFunction<? super Object, ? super Object, ?> remappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized Object merge(Object key, Object value, BiFunction<? super Object, ? super Object, ?> remappingFunction) {
        throw new UnsupportedOperationException();
    }

    public UnmodifiableProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public synchronized Object setProperty(String key, String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized void load(Reader reader) {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized void load(InputStream inStream) {
        throw new UnsupportedOperationException();
    }


    @Override
    public void store(Writer writer, String comments) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void store(OutputStream out, String comments) {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized void loadFromXML(InputStream in) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void storeToXML(OutputStream os, String comment) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void storeToXML(OutputStream os, String comment, String encoding) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    @Override
    public Enumeration<?> propertyNames() {
        return properties.propertyNames();
    }

    @Override
    public Set<String> stringPropertyNames() {
        return properties.stringPropertyNames();
    }

    @Override
    public void list(PrintStream out) {
        properties.list(out);
    }

    @Override
    public void list(PrintWriter out) {
        properties.list(out);
    }
}
