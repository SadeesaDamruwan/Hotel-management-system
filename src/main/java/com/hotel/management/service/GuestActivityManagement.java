package com.hotel.management.service;

import com.hotel.management.model.GuestActivity;
import java.util.*;
import java.text.SimpleDateFormat;

public class GuestActivityManagement {

    private final List<GuestActivity> activities = new ArrayList<>();
    private int nextId = 1;

    public GuestActivityManagement() { }

    public synchronized GuestActivity addActivity(String guestId, String type, String description) {
        Date now = new Date();
        GuestActivity a = new GuestActivity(nextId++, guestId, type, description, now);
        activities.add(a);
        return a;
    }

    public synchronized boolean removeActivity(int id) {
        return activities.removeIf(a -> a.getId() == id);
    }

    public synchronized List<GuestActivity> getAllActivities() {
        return new ArrayList<>(activities);
    }

    public synchronized List<GuestActivity> searchByGuestId(String guestId) {
        List<GuestActivity> out = new ArrayList<>();
        for (GuestActivity a : activities) {
            if (a.getGuestId().equalsIgnoreCase(guestId)) out.add(a);
        }
        return out;
    }

    public synchronized void clearAll() {
        activities.clear();
        nextId = 1;
    }

    public static String formatDate(Date d) {
        if (d == null) return "";
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(d);
    }
}
