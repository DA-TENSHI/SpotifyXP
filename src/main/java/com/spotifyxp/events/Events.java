package com.spotifyxp.events;

import java.util.ArrayList;

public class Events {
    static final ArrayList<Runnable> queueUpdateEvents = new ArrayList<>();
    static final ArrayList<Runnable> queueAdvanceEvents = new ArrayList<>();
    static final ArrayList<Runnable> queueRegressEvents = new ArrayList<>();
    static final ArrayList<Runnable> playerLockReleaseEvents = new ArrayList<>();
    static final ArrayList<Runnable> onFrameReadyEvents = new ArrayList<>();
    static final ArrayList<Runnable> trackNextEvents = new ArrayList<>();
    static final ArrayList<Runnable> trackLoadEvents = new ArrayList<>();
    static final ArrayList<Runnable> trackLoadFinishedEvents = new ArrayList<>();


    private static void createThreadWith(ArrayList<Runnable> toWorkOn) {
        Thread t = new Thread(() -> {
            for(Runnable run : toWorkOn) {
                run.run();
            }
        });
        t.start();
    }

    private static ArrayList<Runnable> getCopy(ArrayList<Runnable> runnables) {
        //Prevents the concurrentModificationException issue when runnables are trying to remove themselfes
        return new ArrayList<>(runnables);
    }

    public static void registerToQueueUpdateEvent(Runnable runnable) {
        queueUpdateEvents.add(runnable);
    }

    public static void INTERNALtriggerQueueUpdateEvents() {
        createThreadWith(getCopy(queueUpdateEvents));
    }

    public static void registerToQueueAdvanceEvent(Runnable runnable) {
        queueAdvanceEvents.add(runnable);
    }

    public static void INTERNALtriggerQueueAdvanceEvents() {
        createThreadWith(getCopy(queueAdvanceEvents));
    }

    public static void registerToQueueRegressEvent(Runnable runnable) {
        queueRegressEvents.add(runnable);
    }

    public static void INTERNALtriggerQueueRegressEvents() {
        createThreadWith(getCopy(queueRegressEvents));
    }

    public static void registerPlayerLockReleaseEvent(Runnable runnable) {
        playerLockReleaseEvents.add(runnable);
    }

    public static void INTERNALtriggerPlayerLockReleaseEvents() {
        createThreadWith(getCopy(playerLockReleaseEvents));
    }

    public static void registerOnFrameReadyEvent(Runnable runnable) {
        onFrameReadyEvents.add(runnable);
    }

    public static void INTERNALtriggerOnFrameReadyEvents() {
        createThreadWith(getCopy(onFrameReadyEvents));
    }

    public static void registerOnTrackLoadEvent(Runnable runnable) {
        trackLoadEvents.add(runnable);
    }

    public static void INTERNALtriggerOnTrackLoadEvents() {
        createThreadWith(getCopy(trackLoadEvents));
    }

    public static void registerOnTrackNextEvent(Runnable runnable) {
        trackNextEvents.add(runnable);
    }

    public static void INTERNALtriggerOnTrackNextEvents() {
        createThreadWith(getCopy(trackNextEvents));
    }

    public static void registerOnTrackLoadFinished(Runnable runnable) {
        trackLoadFinishedEvents.add(runnable);
    }

    public static void INTERNALtriggerOnTrackLoadFinishedEvents() {
        createThreadWith(getCopy(trackLoadFinishedEvents));
    }

    public static void remove(Runnable runnable) {
        queueRegressEvents.remove(runnable);
        queueUpdateEvents.remove(runnable);
        queueAdvanceEvents.remove(runnable);
        playerLockReleaseEvents.remove(runnable);
        onFrameReadyEvents.remove(runnable);
        trackLoadEvents.remove(runnable);
        trackNextEvents.remove(runnable);
    }
}
