/**
 * Autogenerated by Thrift Compiler (0.9.2)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.mustbe.consulo.profiler;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.2)", date = "2015-6-3")
public class MemoryInfo implements org.apache.thrift.TBase<MemoryInfo, MemoryInfo._Fields>, java.io.Serializable, Cloneable, Comparable<MemoryInfo> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("MemoryInfo");

  private static final org.apache.thrift.protocol.TField HEAP_FIELD_DESC = new org.apache.thrift.protocol.TField("heap", org.apache.thrift.protocol.TType.STRUCT, (short)1);
  private static final org.apache.thrift.protocol.TField NON_HEAP_FIELD_DESC = new org.apache.thrift.protocol.TField("nonHeap", org.apache.thrift.protocol.TType.STRUCT, (short)2);
  private static final org.apache.thrift.protocol.TField OBJECT_PENDING_FINALIZATION_COUNT_FIELD_DESC = new org.apache.thrift.protocol.TField("objectPendingFinalizationCount", org.apache.thrift.protocol.TType.I32, (short)3);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new MemoryInfoStandardSchemeFactory());
    schemes.put(TupleScheme.class, new MemoryInfoTupleSchemeFactory());
  }

  public MemoryUsage heap; // required
  public MemoryUsage nonHeap; // required
  public int objectPendingFinalizationCount; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    HEAP((short)1, "heap"),
    NON_HEAP((short)2, "nonHeap"),
    OBJECT_PENDING_FINALIZATION_COUNT((short)3, "objectPendingFinalizationCount");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // HEAP
          return HEAP;
        case 2: // NON_HEAP
          return NON_HEAP;
        case 3: // OBJECT_PENDING_FINALIZATION_COUNT
          return OBJECT_PENDING_FINALIZATION_COUNT;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __OBJECTPENDINGFINALIZATIONCOUNT_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.HEAP, new org.apache.thrift.meta_data.FieldMetaData("heap", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, MemoryUsage.class)));
    tmpMap.put(_Fields.NON_HEAP, new org.apache.thrift.meta_data.FieldMetaData("nonHeap", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, MemoryUsage.class)));
    tmpMap.put(_Fields.OBJECT_PENDING_FINALIZATION_COUNT, new org.apache.thrift.meta_data.FieldMetaData("objectPendingFinalizationCount", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(MemoryInfo.class, metaDataMap);
  }

  public MemoryInfo() {
  }

  public MemoryInfo(
    MemoryUsage heap,
    MemoryUsage nonHeap,
    int objectPendingFinalizationCount)
  {
    this();
    this.heap = heap;
    this.nonHeap = nonHeap;
    this.objectPendingFinalizationCount = objectPendingFinalizationCount;
    setObjectPendingFinalizationCountIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public MemoryInfo(MemoryInfo other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetHeap()) {
      this.heap = new MemoryUsage(other.heap);
    }
    if (other.isSetNonHeap()) {
      this.nonHeap = new MemoryUsage(other.nonHeap);
    }
    this.objectPendingFinalizationCount = other.objectPendingFinalizationCount;
  }

  public MemoryInfo deepCopy() {
    return new MemoryInfo(this);
  }

  @Override
  public void clear() {
    this.heap = null;
    this.nonHeap = null;
    setObjectPendingFinalizationCountIsSet(false);
    this.objectPendingFinalizationCount = 0;
  }

  public MemoryUsage getHeap() {
    return this.heap;
  }

  public MemoryInfo setHeap(MemoryUsage heap) {
    this.heap = heap;
    return this;
  }

  public void unsetHeap() {
    this.heap = null;
  }

  /** Returns true if field heap is set (has been assigned a value) and false otherwise */
  public boolean isSetHeap() {
    return this.heap != null;
  }

  public void setHeapIsSet(boolean value) {
    if (!value) {
      this.heap = null;
    }
  }

  public MemoryUsage getNonHeap() {
    return this.nonHeap;
  }

  public MemoryInfo setNonHeap(MemoryUsage nonHeap) {
    this.nonHeap = nonHeap;
    return this;
  }

  public void unsetNonHeap() {
    this.nonHeap = null;
  }

  /** Returns true if field nonHeap is set (has been assigned a value) and false otherwise */
  public boolean isSetNonHeap() {
    return this.nonHeap != null;
  }

  public void setNonHeapIsSet(boolean value) {
    if (!value) {
      this.nonHeap = null;
    }
  }

  public int getObjectPendingFinalizationCount() {
    return this.objectPendingFinalizationCount;
  }

  public MemoryInfo setObjectPendingFinalizationCount(int objectPendingFinalizationCount) {
    this.objectPendingFinalizationCount = objectPendingFinalizationCount;
    setObjectPendingFinalizationCountIsSet(true);
    return this;
  }

  public void unsetObjectPendingFinalizationCount() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __OBJECTPENDINGFINALIZATIONCOUNT_ISSET_ID);
  }

  /** Returns true if field objectPendingFinalizationCount is set (has been assigned a value) and false otherwise */
  public boolean isSetObjectPendingFinalizationCount() {
    return EncodingUtils.testBit(__isset_bitfield, __OBJECTPENDINGFINALIZATIONCOUNT_ISSET_ID);
  }

  public void setObjectPendingFinalizationCountIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __OBJECTPENDINGFINALIZATIONCOUNT_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case HEAP:
      if (value == null) {
        unsetHeap();
      } else {
        setHeap((MemoryUsage)value);
      }
      break;

    case NON_HEAP:
      if (value == null) {
        unsetNonHeap();
      } else {
        setNonHeap((MemoryUsage)value);
      }
      break;

    case OBJECT_PENDING_FINALIZATION_COUNT:
      if (value == null) {
        unsetObjectPendingFinalizationCount();
      } else {
        setObjectPendingFinalizationCount((Integer)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case HEAP:
      return getHeap();

    case NON_HEAP:
      return getNonHeap();

    case OBJECT_PENDING_FINALIZATION_COUNT:
      return Integer.valueOf(getObjectPendingFinalizationCount());

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case HEAP:
      return isSetHeap();
    case NON_HEAP:
      return isSetNonHeap();
    case OBJECT_PENDING_FINALIZATION_COUNT:
      return isSetObjectPendingFinalizationCount();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof MemoryInfo)
      return this.equals((MemoryInfo)that);
    return false;
  }

  public boolean equals(MemoryInfo that) {
    if (that == null)
      return false;

    boolean this_present_heap = true && this.isSetHeap();
    boolean that_present_heap = true && that.isSetHeap();
    if (this_present_heap || that_present_heap) {
      if (!(this_present_heap && that_present_heap))
        return false;
      if (!this.heap.equals(that.heap))
        return false;
    }

    boolean this_present_nonHeap = true && this.isSetNonHeap();
    boolean that_present_nonHeap = true && that.isSetNonHeap();
    if (this_present_nonHeap || that_present_nonHeap) {
      if (!(this_present_nonHeap && that_present_nonHeap))
        return false;
      if (!this.nonHeap.equals(that.nonHeap))
        return false;
    }

    boolean this_present_objectPendingFinalizationCount = true;
    boolean that_present_objectPendingFinalizationCount = true;
    if (this_present_objectPendingFinalizationCount || that_present_objectPendingFinalizationCount) {
      if (!(this_present_objectPendingFinalizationCount && that_present_objectPendingFinalizationCount))
        return false;
      if (this.objectPendingFinalizationCount != that.objectPendingFinalizationCount)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_heap = true && (isSetHeap());
    list.add(present_heap);
    if (present_heap)
      list.add(heap);

    boolean present_nonHeap = true && (isSetNonHeap());
    list.add(present_nonHeap);
    if (present_nonHeap)
      list.add(nonHeap);

    boolean present_objectPendingFinalizationCount = true;
    list.add(present_objectPendingFinalizationCount);
    if (present_objectPendingFinalizationCount)
      list.add(objectPendingFinalizationCount);

    return list.hashCode();
  }

  @Override
  public int compareTo(MemoryInfo other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetHeap()).compareTo(other.isSetHeap());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetHeap()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.heap, other.heap);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetNonHeap()).compareTo(other.isSetNonHeap());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetNonHeap()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.nonHeap, other.nonHeap);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetObjectPendingFinalizationCount()).compareTo(other.isSetObjectPendingFinalizationCount());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetObjectPendingFinalizationCount()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.objectPendingFinalizationCount, other.objectPendingFinalizationCount);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("MemoryInfo(");
    boolean first = true;

    sb.append("heap:");
    if (this.heap == null) {
      sb.append("null");
    } else {
      sb.append(this.heap);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("nonHeap:");
    if (this.nonHeap == null) {
      sb.append("null");
    } else {
      sb.append(this.nonHeap);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("objectPendingFinalizationCount:");
    sb.append(this.objectPendingFinalizationCount);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
    if (heap != null) {
      heap.validate();
    }
    if (nonHeap != null) {
      nonHeap.validate();
    }
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class MemoryInfoStandardSchemeFactory implements SchemeFactory {
    public MemoryInfoStandardScheme getScheme() {
      return new MemoryInfoStandardScheme();
    }
  }

  private static class MemoryInfoStandardScheme extends StandardScheme<MemoryInfo> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, MemoryInfo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // HEAP
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.heap = new MemoryUsage();
              struct.heap.read(iprot);
              struct.setHeapIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // NON_HEAP
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.nonHeap = new MemoryUsage();
              struct.nonHeap.read(iprot);
              struct.setNonHeapIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // OBJECT_PENDING_FINALIZATION_COUNT
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.objectPendingFinalizationCount = iprot.readI32();
              struct.setObjectPendingFinalizationCountIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, MemoryInfo struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.heap != null) {
        oprot.writeFieldBegin(HEAP_FIELD_DESC);
        struct.heap.write(oprot);
        oprot.writeFieldEnd();
      }
      if (struct.nonHeap != null) {
        oprot.writeFieldBegin(NON_HEAP_FIELD_DESC);
        struct.nonHeap.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(OBJECT_PENDING_FINALIZATION_COUNT_FIELD_DESC);
      oprot.writeI32(struct.objectPendingFinalizationCount);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class MemoryInfoTupleSchemeFactory implements SchemeFactory {
    public MemoryInfoTupleScheme getScheme() {
      return new MemoryInfoTupleScheme();
    }
  }

  private static class MemoryInfoTupleScheme extends TupleScheme<MemoryInfo> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, MemoryInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetHeap()) {
        optionals.set(0);
      }
      if (struct.isSetNonHeap()) {
        optionals.set(1);
      }
      if (struct.isSetObjectPendingFinalizationCount()) {
        optionals.set(2);
      }
      oprot.writeBitSet(optionals, 3);
      if (struct.isSetHeap()) {
        struct.heap.write(oprot);
      }
      if (struct.isSetNonHeap()) {
        struct.nonHeap.write(oprot);
      }
      if (struct.isSetObjectPendingFinalizationCount()) {
        oprot.writeI32(struct.objectPendingFinalizationCount);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, MemoryInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(3);
      if (incoming.get(0)) {
        struct.heap = new MemoryUsage();
        struct.heap.read(iprot);
        struct.setHeapIsSet(true);
      }
      if (incoming.get(1)) {
        struct.nonHeap = new MemoryUsage();
        struct.nonHeap.read(iprot);
        struct.setNonHeapIsSet(true);
      }
      if (incoming.get(2)) {
        struct.objectPendingFinalizationCount = iprot.readI32();
        struct.setObjectPendingFinalizationCountIsSet(true);
      }
    }
  }

}

