/*
 * Copyright (c) 2019. MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licenced with MIT:
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 *  and associated documentation files (the "Software"), to deal in the Software without restriction,including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *   The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *    SOFTWARE.
 */

package com.developerfromjokela.motioneyeclient.classes;

import com.developerfromjokela.motioneyeclient.classes.disks.Disk;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Camera {


    @SerializedName("show_frame_changes")
    private boolean show_frame_changes;
    @SerializedName("actions")
    private List<String> actions;
    @SerializedName("preserve_movies")
    private String preserve_movies;
    @SerializedName("recording_mode")
    private String recording_mode;
    @SerializedName("monday_to")
    private String monday_to;
    @SerializedName("light_switch_detect")
    private String light_switch_detect;
    @SerializedName("command_end_notifications_enabled")
    private boolean command_end_notifications_enabled;
    @SerializedName("smb_shares")
    private boolean smb_shares;
    @SerializedName("upload_server")
    private String upload_server;
    @SerializedName("monday_from")
    private String monday_from;
    @SerializedName("audo_brightness")
    private boolean audo_brightness;
    @SerializedName("frame_change_threshold")
    private String frame_change_threshold;
    @SerializedName("name")
    private String name;
    @SerializedName("movie_format")
    private String movie_format;
    @SerializedName("network_username")
    private String network_username;
    @SerializedName("preserve_pictures")
    private String preserve_pictures;
    @SerializedName("event_gap")
    private String event_gap;
    @SerializedName("capture_mode")
    private String capture_mode;
    @SerializedName("upload_movie")
    private boolean upload_movie;
    @SerializedName("video_streaming")
    private boolean video_streaming;
    @SerializedName("upload_location")
    private String upload_location;
    @SerializedName("max_movie_length")
    private String max_movie_length;
    @SerializedName("movie_file_name")
    private String movie_file_name;
    @SerializedName("upload_authorization_key")
    private String upload_authorization_key;
    @SerializedName("still_images")
    private boolean still_images;
    @SerializedName("upload_method")

    private String upload_method;
    @SerializedName("streaming_resolution")

    private String streaming_resolution;
    @SerializedName("device_url")

    private String device_url;
    @SerializedName("text_overlay")

    private boolean text_overlay;
    @SerializedName("right_text")

    private String right_text;
    @SerializedName("upload_picture")

    private boolean upload_picture;
    @SerializedName("email_notifications_enabled")

    private boolean email_notifications_enabled;
    @SerializedName("working_schedule_type")

    private String working_schedule_type;
    @SerializedName("enabled")

    private boolean enabled;
    @SerializedName("movie_quality")

    private String movie_quality;
    @SerializedName("disk_total")

    private String disk_total;
    @SerializedName("upload_service")

    private String upload_service;
    @SerializedName("upload_password")

    private String upload_password;
    @SerializedName("wednesday_to")

    private String wednesday_to;
    @SerializedName("mask_type")

    private String mask_type;
    @SerializedName("command_storage_enabled")

    private boolean command_storage_enabled;
    @SerializedName("disk_used")

    private String disk_used;
    @SerializedName("streaming_motion")

    private String streaming_motion;
    @SerializedName("manual_snapshots")

    private boolean manual_snapshots;
    @SerializedName("noise_level")

    private String noise_level;
    @SerializedName("mask_lines")

    private List<String> mask_lines;
    @SerializedName("upload_enabled")

    private boolean upload_enabled;
    @SerializedName("root_directory")

    private String root_directory;
    @SerializedName("working_schedule")

    private boolean working_schedule;
    @SerializedName("pre_capture")

    private String pre_capture;
    @SerializedName("command_notifications_enabled")

    private boolean command_notifications_enabled;
    @SerializedName("streaming_framerate")

    private String streaming_framerate;
    @SerializedName("email_notifications_picture_time_span")

    private String email_notifications_picture_time_span;
    @SerializedName("thursday_to")

    private String thursday_to;
    @SerializedName("streaming_server_resize")

    private boolean streaming_server_resize;
    @SerializedName("upload_subfolders")

    private boolean upload_subfolders;
    @SerializedName("sunday_to")

    private String sunday_to;
    @SerializedName("left_text")

    private String left_text;
    @SerializedName("image_file_name")

    private String image_file_name;
    @SerializedName("rotation")

    private String rotation;
    @SerializedName("framerate")

    private String framerate;
    @SerializedName("movies")

    private boolean movies;
    @SerializedName("motion_detection")

    private boolean motion_detection;
    @SerializedName("upload_username")

    private String upload_username;
    @SerializedName("upload_port")

    private String upload_port;
    @SerializedName("available_disks")

    private List<Disk> available_disks;
    @SerializedName("network_smb_ver")

    private String network_smb_ver;
    @SerializedName("streaming_auth_mode")

    private String streaming_auth_mode;
    @SerializedName("despeckle_filter")

    private String despeckle_filter;
    @SerializedName("snapshot_Stringerval")

    private String snapshot_Stringerval;
    @SerializedName("minimum_motion_frames")

    private String minimum_motion_frames;

    @SerializedName("auto_noise_detect")
    private boolean auto_noise_detect;
    @SerializedName("network_share_name")

    private String network_share_name;
    @SerializedName("sunday_from")

    private String sunday_from;
    @SerializedName("friday_from")

    private String friday_from;
    @SerializedName("web_hook_storage_enabled")

    private boolean web_hook_storage_enabled;
    @SerializedName("custom_left_text")

    private String custom_left_text;
    @SerializedName("streaming_port")

    private String streaming_port;
    @SerializedName("id")

    private String id;
    @SerializedName("post_capture")

    private String post_capture;
    @SerializedName("streaming_quality")

    private String streaming_quality;
    @SerializedName("wednesday_from")

    private String wednesday_from;
    @SerializedName("proto")

    private String proto;
    @SerializedName("extra_options")

    private List<String> extra_options;
    @SerializedName("image_quality")

    private String image_quality;
    @SerializedName("create_debug_media")

    private boolean create_debug_media;
    @SerializedName("friday_to")

    private String friday_to;
    @SerializedName("custom_right_text")

    private String custom_right_text;
    @SerializedName("web_hook_notifications_enabled")

    private boolean web_hook_notifications_enabled;
    @SerializedName("saturday_from")

    private String saturday_from;
    @SerializedName("tuesday_from")

    private String tuesday_from;
    @SerializedName("network_password")

    private String network_password;
    @SerializedName("saturday_to")

    private String saturday_to;
    @SerializedName("newtork_server")

    private String newtork_server;
    @SerializedName("smart_mask_sluggishness")

    private String smart_mask_sluggishness;
    @SerializedName("mask")

    private boolean mask;
    @SerializedName("tuesday_to")

    private String tuesday_to;
    @SerializedName("thursday_from")

    private String thursday_from;
    @SerializedName("storage_device")

    private String storage_device;

    public Camera(boolean show_frame_changes, List<String> actions, String preserve_movies, String recording_mode, String monday_to, String light_switch_detect, boolean command_end_notifications_enabled, boolean smb_shares, String upload_server, String monday_from, boolean audo_brightness, String frame_change_threshold, String name, String movie_format, String network_username, String preserve_pictures, String event_gap, String capture_mode, boolean upload_movie, boolean video_streaming, String upload_location, String max_movie_length, String movie_file_name, String upload_authorization_key, boolean still_images, String upload_method, String streaming_resolution, String device_url, boolean text_overlay, String right_text, boolean upload_picture, boolean email_notifications_enabled, String working_schedule_type, boolean enabled, String movie_quality, String disk_total, String upload_service, String upload_password, String wednesday_to, String mask_type, boolean command_storage_enabled, String disk_used, String streaming_motion, boolean manual_snapshots, String noise_level, List<String> mask_lines, boolean upload_enabled, String root_directory, boolean working_schedule, String pre_capture, boolean command_notifications_enabled, String streaming_framerate, String email_notifications_picture_time_span, String thursday_to, boolean streaming_server_resize, boolean upload_subfolders, String sunday_to, String left_text, String image_file_name, String rotation, String framerate, boolean movies, boolean motion_detection, String upload_username, String upload_port, List<Disk> available_disks, String network_smb_ver, String streaming_auth_mode, String despeckle_filter, String snapshot_Stringerval, String minimum_motion_frames, boolean auto_noise_detect, String network_share_name, String sunday_from, String friday_from, boolean web_hook_storage_enabled, String custom_left_text, String streaming_port, String id, String post_capture, String streaming_quality, String wednesday_from, String proto, List<String> extra_options, String image_quality, boolean create_debug_media, String friday_to, String custom_right_text, boolean web_hook_notifications_enabled, String saturday_from, String tuesday_from, String network_password, String saturday_to, String newtork_server, String smart_mask_sluggishness, boolean mask, String tuesday_to, String thursday_from, String storage_device) {
        this.show_frame_changes = show_frame_changes;
        this.actions = actions;
        this.preserve_movies = preserve_movies;
        this.recording_mode = recording_mode;
        this.monday_to = monday_to;
        this.light_switch_detect = light_switch_detect;
        this.command_end_notifications_enabled = command_end_notifications_enabled;
        this.smb_shares = smb_shares;
        this.upload_server = upload_server;
        this.monday_from = monday_from;
        this.audo_brightness = audo_brightness;
        this.frame_change_threshold = frame_change_threshold;
        this.name = name;
        this.movie_format = movie_format;
        this.network_username = network_username;
        this.preserve_pictures = preserve_pictures;
        this.event_gap = event_gap;
        this.capture_mode = capture_mode;
        this.upload_movie = upload_movie;
        this.video_streaming = video_streaming;
        this.upload_location = upload_location;
        this.max_movie_length = max_movie_length;
        this.movie_file_name = movie_file_name;
        this.upload_authorization_key = upload_authorization_key;
        this.still_images = still_images;
        this.upload_method = upload_method;
        this.streaming_resolution = streaming_resolution;
        this.device_url = device_url;
        this.text_overlay = text_overlay;
        this.right_text = right_text;
        this.upload_picture = upload_picture;
        this.email_notifications_enabled = email_notifications_enabled;
        this.working_schedule_type = working_schedule_type;
        this.enabled = enabled;
        this.movie_quality = movie_quality;
        this.disk_total = disk_total;
        this.upload_service = upload_service;
        this.upload_password = upload_password;
        this.wednesday_to = wednesday_to;
        this.mask_type = mask_type;
        this.command_storage_enabled = command_storage_enabled;
        this.disk_used = disk_used;
        this.streaming_motion = streaming_motion;
        this.manual_snapshots = manual_snapshots;
        this.noise_level = noise_level;
        this.mask_lines = mask_lines;
        this.upload_enabled = upload_enabled;
        this.root_directory = root_directory;
        this.working_schedule = working_schedule;
        this.pre_capture = pre_capture;
        this.command_notifications_enabled = command_notifications_enabled;
        this.streaming_framerate = streaming_framerate;
        this.email_notifications_picture_time_span = email_notifications_picture_time_span;
        this.thursday_to = thursday_to;
        this.streaming_server_resize = streaming_server_resize;
        this.upload_subfolders = upload_subfolders;
        this.sunday_to = sunday_to;
        this.left_text = left_text;
        this.image_file_name = image_file_name;
        this.rotation = rotation;
        this.framerate = framerate;
        this.movies = movies;
        this.motion_detection = motion_detection;
        this.upload_username = upload_username;
        this.upload_port = upload_port;
        this.available_disks = available_disks;
        this.network_smb_ver = network_smb_ver;
        this.streaming_auth_mode = streaming_auth_mode;
        this.despeckle_filter = despeckle_filter;
        this.snapshot_Stringerval = snapshot_Stringerval;
        this.minimum_motion_frames = minimum_motion_frames;
        this.auto_noise_detect = auto_noise_detect;
        this.network_share_name = network_share_name;
        this.sunday_from = sunday_from;
        this.friday_from = friday_from;
        this.web_hook_storage_enabled = web_hook_storage_enabled;
        this.custom_left_text = custom_left_text;
        this.streaming_port = streaming_port;
        this.id = id;
        this.post_capture = post_capture;
        this.streaming_quality = streaming_quality;
        this.wednesday_from = wednesday_from;
        this.proto = proto;
        this.extra_options = extra_options;
        this.image_quality = image_quality;
        this.create_debug_media = create_debug_media;
        this.friday_to = friday_to;
        this.custom_right_text = custom_right_text;
        this.web_hook_notifications_enabled = web_hook_notifications_enabled;
        this.saturday_from = saturday_from;
        this.tuesday_from = tuesday_from;
        this.network_password = network_password;
        this.saturday_to = saturday_to;
        this.newtork_server = newtork_server;
        this.smart_mask_sluggishness = smart_mask_sluggishness;
        this.mask = mask;
        this.tuesday_to = tuesday_to;
        this.thursday_from = thursday_from;
        this.storage_device = storage_device;
    }

    public boolean isShow_frame_changes() {
        return show_frame_changes;
    }

    public void setShow_frame_changes(boolean show_frame_changes) {
        this.show_frame_changes = show_frame_changes;
    }

    public List<String> getActions() {
        return actions;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }

    public String getPreserve_movies() {
        return preserve_movies;
    }

    public void setPreserve_movies(String preserve_movies) {
        this.preserve_movies = preserve_movies;
    }

    public String getRecording_mode() {
        return recording_mode;
    }

    public void setRecording_mode(String recording_mode) {
        this.recording_mode = recording_mode;
    }

    public String getMonday_to() {
        return monday_to;
    }

    public void setMonday_to(String monday_to) {
        this.monday_to = monday_to;
    }

    public String getLight_switch_detect() {
        return light_switch_detect;
    }

    public void setLight_switch_detect(String light_switch_detect) {
        this.light_switch_detect = light_switch_detect;
    }

    public boolean isCommand_end_notifications_enabled() {
        return command_end_notifications_enabled;
    }

    public void setCommand_end_notifications_enabled(boolean command_end_notifications_enabled) {
        this.command_end_notifications_enabled = command_end_notifications_enabled;
    }

    public boolean isSmb_shares() {
        return smb_shares;
    }

    public void setSmb_shares(boolean smb_shares) {
        this.smb_shares = smb_shares;
    }

    public String getUpload_server() {
        return upload_server;
    }

    public void setUpload_server(String upload_server) {
        this.upload_server = upload_server;
    }

    public String getMonday_from() {
        return monday_from;
    }

    public void setMonday_from(String monday_from) {
        this.monday_from = monday_from;
    }

    public boolean isAudo_brightness() {
        return audo_brightness;
    }

    public void setAudo_brightness(boolean audo_brightness) {
        this.audo_brightness = audo_brightness;
    }

    public String getFrame_change_threshold() {
        return frame_change_threshold;
    }

    public void setFrame_change_threshold(String frame_change_threshold) {
        this.frame_change_threshold = frame_change_threshold;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMovie_format() {
        return movie_format;
    }

    public void setMovie_format(String movie_format) {
        this.movie_format = movie_format;
    }

    public String getNetwork_username() {
        return network_username;
    }

    public void setNetwork_username(String network_username) {
        this.network_username = network_username;
    }

    public String getPreserve_pictures() {
        return preserve_pictures;
    }

    public void setPreserve_pictures(String preserve_pictures) {
        this.preserve_pictures = preserve_pictures;
    }

    public String getEvent_gap() {
        return event_gap;
    }

    public void setEvent_gap(String event_gap) {
        this.event_gap = event_gap;
    }

    public String getCapture_mode() {
        return capture_mode;
    }

    public void setCapture_mode(String capture_mode) {
        this.capture_mode = capture_mode;
    }

    public boolean isUpload_movie() {
        return upload_movie;
    }

    public void setUpload_movie(boolean upload_movie) {
        this.upload_movie = upload_movie;
    }

    public boolean isVideo_streaming() {
        return video_streaming;
    }

    public void setVideo_streaming(boolean video_streaming) {
        this.video_streaming = video_streaming;
    }

    public String getUpload_location() {
        return upload_location;
    }

    public void setUpload_location(String upload_location) {
        this.upload_location = upload_location;
    }

    public String getMax_movie_length() {
        return max_movie_length;
    }

    public void setMax_movie_length(String max_movie_length) {
        this.max_movie_length = max_movie_length;
    }

    public String getMovie_file_name() {
        return movie_file_name;
    }

    public void setMovie_file_name(String movie_file_name) {
        this.movie_file_name = movie_file_name;
    }

    public String getUpload_authorization_key() {
        return upload_authorization_key;
    }

    public void setUpload_authorization_key(String upload_authorization_key) {
        this.upload_authorization_key = upload_authorization_key;
    }

    public boolean isStill_images() {
        return still_images;
    }

    public void setStill_images(boolean still_images) {
        this.still_images = still_images;
    }

    public String getUpload_method() {
        return upload_method;
    }

    public void setUpload_method(String upload_method) {
        this.upload_method = upload_method;
    }

    public String getStreaming_resolution() {
        return streaming_resolution;
    }

    public void setStreaming_resolution(String streaming_resolution) {
        this.streaming_resolution = streaming_resolution;
    }

    public String getDevice_url() {
        return device_url;
    }

    public void setDevice_url(String device_url) {
        this.device_url = device_url;
    }

    public boolean isText_overlay() {
        return text_overlay;
    }

    public void setText_overlay(boolean text_overlay) {
        this.text_overlay = text_overlay;
    }

    public String getRight_text() {
        return right_text;
    }

    public void setRight_text(String right_text) {
        this.right_text = right_text;
    }

    public boolean isUpload_picture() {
        return upload_picture;
    }

    public void setUpload_picture(boolean upload_picture) {
        this.upload_picture = upload_picture;
    }

    public boolean isEmail_notifications_enabled() {
        return email_notifications_enabled;
    }

    public void setEmail_notifications_enabled(boolean email_notifications_enabled) {
        this.email_notifications_enabled = email_notifications_enabled;
    }

    public String getWorking_schedule_type() {
        return working_schedule_type;
    }

    public void setWorking_schedule_type(String working_schedule_type) {
        this.working_schedule_type = working_schedule_type;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getMovie_quality() {
        return movie_quality;
    }

    public void setMovie_quality(String movie_quality) {
        this.movie_quality = movie_quality;
    }

    public String getDisk_total() {
        return disk_total;
    }

    public void setDisk_total(String disk_total) {
        this.disk_total = disk_total;
    }

    public String getUpload_service() {
        return upload_service;
    }

    public void setUpload_service(String upload_service) {
        this.upload_service = upload_service;
    }

    public String getUpload_password() {
        return upload_password;
    }

    public void setUpload_password(String upload_password) {
        this.upload_password = upload_password;
    }

    public String getWednesday_to() {
        return wednesday_to;
    }

    public void setWednesday_to(String wednesday_to) {
        this.wednesday_to = wednesday_to;
    }

    public String getMask_type() {
        return mask_type;
    }

    public void setMask_type(String mask_type) {
        this.mask_type = mask_type;
    }

    public boolean isCommand_storage_enabled() {
        return command_storage_enabled;
    }

    public void setCommand_storage_enabled(boolean command_storage_enabled) {
        this.command_storage_enabled = command_storage_enabled;
    }

    public String getDisk_used() {
        return disk_used;
    }

    public void setDisk_used(String disk_used) {
        this.disk_used = disk_used;
    }

    public String getStreaming_motion() {
        return streaming_motion;
    }

    public void setStreaming_motion(String streaming_motion) {
        this.streaming_motion = streaming_motion;
    }

    public boolean isManual_snapshots() {
        return manual_snapshots;
    }

    public void setManual_snapshots(boolean manual_snapshots) {
        this.manual_snapshots = manual_snapshots;
    }

    public String getNoise_level() {
        return noise_level;
    }

    public void setNoise_level(String noise_level) {
        this.noise_level = noise_level;
    }

    public List<String> getMask_lines() {
        return mask_lines;
    }

    public void setMask_lines(List<String> mask_lines) {
        this.mask_lines = mask_lines;
    }

    public boolean isUpload_enabled() {
        return upload_enabled;
    }

    public void setUpload_enabled(boolean upload_enabled) {
        this.upload_enabled = upload_enabled;
    }

    public String getRoot_directory() {
        return root_directory;
    }

    public void setRoot_directory(String root_directory) {
        this.root_directory = root_directory;
    }

    public boolean isWorking_schedule() {
        return working_schedule;
    }

    public void setWorking_schedule(boolean working_schedule) {
        this.working_schedule = working_schedule;
    }

    public String getPre_capture() {
        return pre_capture;
    }

    public void setPre_capture(String pre_capture) {
        this.pre_capture = pre_capture;
    }

    public boolean isCommand_notifications_enabled() {
        return command_notifications_enabled;
    }

    public void setCommand_notifications_enabled(boolean command_notifications_enabled) {
        this.command_notifications_enabled = command_notifications_enabled;
    }

    public String getStreaming_framerate() {
        return streaming_framerate;
    }

    public void setStreaming_framerate(String streaming_framerate) {
        this.streaming_framerate = streaming_framerate;
    }

    public String getEmail_notifications_picture_time_span() {
        return email_notifications_picture_time_span;
    }

    public void setEmail_notifications_picture_time_span(String email_notifications_picture_time_span) {
        this.email_notifications_picture_time_span = email_notifications_picture_time_span;
    }

    public String getThursday_to() {
        return thursday_to;
    }

    public void setThursday_to(String thursday_to) {
        this.thursday_to = thursday_to;
    }

    public boolean isStreaming_server_resize() {
        return streaming_server_resize;
    }

    public void setStreaming_server_resize(boolean streaming_server_resize) {
        this.streaming_server_resize = streaming_server_resize;
    }

    public boolean isUpload_subfolders() {
        return upload_subfolders;
    }

    public void setUpload_subfolders(boolean upload_subfolders) {
        this.upload_subfolders = upload_subfolders;
    }

    public String getSunday_to() {
        return sunday_to;
    }

    public void setSunday_to(String sunday_to) {
        this.sunday_to = sunday_to;
    }

    public String getLeft_text() {
        return left_text;
    }

    public void setLeft_text(String left_text) {
        this.left_text = left_text;
    }

    public String getImage_file_name() {
        return image_file_name;
    }

    public void setImage_file_name(String image_file_name) {
        this.image_file_name = image_file_name;
    }

    public String getRotation() {
        return rotation;
    }

    public void setRotation(String rotation) {
        this.rotation = rotation;
    }

    public String getFramerate() {
        return framerate;
    }

    public void setFramerate(String framerate) {
        this.framerate = framerate;
    }

    public boolean isMovies() {
        return movies;
    }

    public void setMovies(boolean movies) {
        this.movies = movies;
    }

    public boolean isMotion_detection() {
        return motion_detection;
    }

    public void setMotion_detection(boolean motion_detection) {
        this.motion_detection = motion_detection;
    }

    public String getUpload_username() {
        return upload_username;
    }

    public void setUpload_username(String upload_username) {
        this.upload_username = upload_username;
    }

    public String getUpload_port() {
        return upload_port;
    }

    public void setUpload_port(String upload_port) {
        this.upload_port = upload_port;
    }

    public List<Disk> getAvailable_disks() {
        return available_disks;
    }

    public void setAvailable_disks(List<Disk> available_disks) {
        this.available_disks = available_disks;
    }

    public String getNetwork_smb_ver() {
        return network_smb_ver;
    }

    public void setNetwork_smb_ver(String network_smb_ver) {
        this.network_smb_ver = network_smb_ver;
    }

    public String getStreaming_auth_mode() {
        return streaming_auth_mode;
    }

    public void setStreaming_auth_mode(String streaming_auth_mode) {
        this.streaming_auth_mode = streaming_auth_mode;
    }

    public String getDespeckle_filter() {
        return despeckle_filter;
    }

    public void setDespeckle_filter(String despeckle_filter) {
        this.despeckle_filter = despeckle_filter;
    }

    public String getSnapshot_Stringerval() {
        return snapshot_Stringerval;
    }

    public void setSnapshot_Stringerval(String snapshot_Stringerval) {
        this.snapshot_Stringerval = snapshot_Stringerval;
    }

    public String getMinimum_motion_frames() {
        return minimum_motion_frames;
    }

    public void setMinimum_motion_frames(String minimum_motion_frames) {
        this.minimum_motion_frames = minimum_motion_frames;
    }

    public boolean isAuto_noise_detect() {
        return auto_noise_detect;
    }

    public void setAuto_noise_detect(boolean auto_noise_detect) {
        this.auto_noise_detect = auto_noise_detect;
    }

    public String getNetwork_share_name() {
        return network_share_name;
    }

    public void setNetwork_share_name(String network_share_name) {
        this.network_share_name = network_share_name;
    }

    public String getSunday_from() {
        return sunday_from;
    }

    public void setSunday_from(String sunday_from) {
        this.sunday_from = sunday_from;
    }

    public String getFriday_from() {
        return friday_from;
    }

    public void setFriday_from(String friday_from) {
        this.friday_from = friday_from;
    }

    public boolean isWeb_hook_storage_enabled() {
        return web_hook_storage_enabled;
    }

    public void setWeb_hook_storage_enabled(boolean web_hook_storage_enabled) {
        this.web_hook_storage_enabled = web_hook_storage_enabled;
    }

    public String getCustom_left_text() {
        return custom_left_text;
    }

    public void setCustom_left_text(String custom_left_text) {
        this.custom_left_text = custom_left_text;
    }

    public String getStreaming_port() {
        return streaming_port;
    }

    public void setStreaming_port(String streaming_port) {
        this.streaming_port = streaming_port;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPost_capture() {
        return post_capture;
    }

    public void setPost_capture(String post_capture) {
        this.post_capture = post_capture;
    }

    public String getStreaming_quality() {
        return streaming_quality;
    }

    public void setStreaming_quality(String streaming_quality) {
        this.streaming_quality = streaming_quality;
    }

    public String getWednesday_from() {
        return wednesday_from;
    }

    public void setWednesday_from(String wednesday_from) {
        this.wednesday_from = wednesday_from;
    }

    public String getProto() {
        return proto;
    }

    public void setProto(String proto) {
        this.proto = proto;
    }

    public List<String> getExtra_options() {
        return extra_options;
    }

    public void setExtra_options(List<String> extra_options) {
        this.extra_options = extra_options;
    }

    public String getImage_quality() {
        return image_quality;
    }

    public void setImage_quality(String image_quality) {
        this.image_quality = image_quality;
    }

    public boolean isCreate_debug_media() {
        return create_debug_media;
    }

    public void setCreate_debug_media(boolean create_debug_media) {
        this.create_debug_media = create_debug_media;
    }

    public String getFriday_to() {
        return friday_to;
    }

    public void setFriday_to(String friday_to) {
        this.friday_to = friday_to;
    }

    public String getCustom_right_text() {
        return custom_right_text;
    }

    public void setCustom_right_text(String custom_right_text) {
        this.custom_right_text = custom_right_text;
    }

    public boolean isWeb_hook_notifications_enabled() {
        return web_hook_notifications_enabled;
    }

    public void setWeb_hook_notifications_enabled(boolean web_hook_notifications_enabled) {
        this.web_hook_notifications_enabled = web_hook_notifications_enabled;
    }

    public String getSaturday_from() {
        return saturday_from;
    }

    public void setSaturday_from(String saturday_from) {
        this.saturday_from = saturday_from;
    }

    public String getTuesday_from() {
        return tuesday_from;
    }

    public void setTuesday_from(String tuesday_from) {
        this.tuesday_from = tuesday_from;
    }

    public String getNetwork_password() {
        return network_password;
    }

    public void setNetwork_password(String network_password) {
        this.network_password = network_password;
    }

    public String getSaturday_to() {
        return saturday_to;
    }

    public void setSaturday_to(String saturday_to) {
        this.saturday_to = saturday_to;
    }

    public String getNewtork_server() {
        return newtork_server;
    }

    public void setNewtork_server(String newtork_server) {
        this.newtork_server = newtork_server;
    }

    public String getSmart_mask_sluggishness() {
        return smart_mask_sluggishness;
    }

    public void setSmart_mask_sluggishness(String smart_mask_sluggishness) {
        this.smart_mask_sluggishness = smart_mask_sluggishness;
    }

    public boolean isMask() {
        return mask;
    }

    public void setMask(boolean mask) {
        this.mask = mask;
    }

    public String getTuesday_to() {
        return tuesday_to;
    }

    public void setTuesday_to(String tuesday_to) {
        this.tuesday_to = tuesday_to;
    }

    public String getThursday_from() {
        return thursday_from;
    }

    public void setThursday_from(String thursday_from) {
        this.thursday_from = thursday_from;
    }

    public String getStorage_device() {
        return storage_device;
    }

    public void setStorage_device(String storage_device) {
        this.storage_device = storage_device;
    }
}
