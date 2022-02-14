// IRoomBeanAIDL.aidl
package com.sencent.dm;
import com.sencent.dm.RoomBean;

// Declare any non-default types here with import statements

interface IRoomBeanAIDL {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
 void addRoomBean(in RoomBean roombean);
}