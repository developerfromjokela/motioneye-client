/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licenced with MIT
 */

package com.developerfromjokela.motioneyeclient.classes.disks;

import java.io.Serializable;

public class Partition implements Serializable {
    private String mount_point;
    private String vendor;
    private String target;
    private int part_no;
    private String bus;
    private String fstype;
    private String model;
    private String opts;

    public Partition(String mount_point, String vendor, String target, int part_no, String bus, String fstype, String model, String opts) {
        this.mount_point = mount_point;
        this.vendor = vendor;
        this.target = target;
        this.part_no = part_no;
        this.bus = bus;
        this.fstype = fstype;
        this.model = model;
        this.opts = opts;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setBus(String bus) {
        this.bus = bus;
    }

    public String getVendor() {
        return vendor;
    }

    public String getTarget() {
        return target;
    }

    public String getModel() {
        return model;
    }

    public String getBus() {
        return bus;
    }

    public int getPart_no() {
        return part_no;
    }

    public String getFstype() {
        return fstype;
    }

    public String getMount_point() {
        return mount_point;
    }

    public String getOpts() {
        return opts;
    }

    public void setFstype(String fstype) {
        this.fstype = fstype;
    }

    public void setMount_point(String mount_point) {
        this.mount_point = mount_point;
    }

    public void setOpts(String opts) {
        this.opts = opts;
    }

    public void setPart_no(int part_no) {
        this.part_no = part_no;
    }
}
