/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licenced with MIT
 */

package com.developerfromjokela.motioneyeclient.classes.disks;

import java.io.Serializable;
import java.util.List;

public class Disk implements Serializable {
    private String bus;
    private String model;
    private String vendor;
    private String target;
    private List<Partition> partitions;

    public Disk(String bus, String model, String vendor, String target, List<Partition> partitions) {
        this.bus = bus;
        this.model = model;
        this.target = target;
        this.vendor = vendor;
        this.partitions = partitions;
    }

    public List<Partition> getPartitions() {
        return partitions;
    }

    public String getBus() {
        return bus;
    }

    public String getModel() {
        return model;
    }

    public String getTarget() {
        return target;
    }

    public String getVendor() {
        return vendor;
    }

    public void setBus(String bus) {
        this.bus = bus;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setPartitions(List<Partition> partitions) {
        this.partitions = partitions;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
}
