package edu.monash.knowledgezoo.api.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SimpleStopWatchTimer {
    /**
     * todo:=> Add the lap functionality ... Make a different interface for laptimes and use the arraylist
     */

    /**
     *
     */
    public enum STATUS {
        READY, RUNNING, PAUSED, STOPPED
    }

    private STATUS status;
    private CompactTime mainTime;

    private long baseStartTime;
    private long resumeStartTime;
    private long pauseStartTime;

    private long pauseTimeAccumulator;
    private long timeAccumulator;

    private ArrayList<CompactTime> laptimes;

    public SimpleStopWatchTimer() {
        mainTime = new CompactTime();
        laptimes = new ArrayList<>();
        reset();
    }

    public void reset() {
        mainTime.zeroTime();
        timeAccumulator = 0;
        pauseTimeAccumulator = 0;
        status = STATUS.READY;

    }

    public void start() {
        switch (status) {
            case RUNNING:
                break;
            case PAUSED:
                resume();
                break;
            case STOPPED:
                reset();
            case READY:
                baseStartTime = System.currentTimeMillis();
                resumeStartTime = baseStartTime;
                status = STATUS.RUNNING;
        }
    }

    public void pause() {
        status = STATUS.PAUSED;
        timeAccumulator += System.currentTimeMillis() - resumeStartTime;
        pauseStartTime = System.currentTimeMillis();
        updateValues();
    }

    public void resume() {
        pauseTimeAccumulator += System.currentTimeMillis() - pauseStartTime;
        resumeStartTime = System.currentTimeMillis();
        status = STATUS.RUNNING;
    }

    public void stop() {
        if (status == STATUS.RUNNING)
            updateAccumulator();
        status = STATUS.STOPPED;
        updateValues();
    }

    public void lap() {
        updateValues();
        laptimes.add(new CompactTime(getTimeElapsed()));
    }

    private void updateAccumulator() {
        long timeSinceResume = (System.currentTimeMillis() - resumeStartTime);
        timeAccumulator += timeSinceResume;
    }

    public long getTimeElapsed() {
        switch (status) {
            case RUNNING:
                long timeSinceResume = (System.currentTimeMillis() - resumeStartTime);
                return timeAccumulator + timeSinceResume;
            default:
                return timeAccumulator;
        }
    }

    private void updateValues() {
        mainTime.setTimeInMilliSeconds(getTimeElapsed());
    }

    public CompactTime getMainTime() {
        updateValues();
        return mainTime;
    }

    public boolean isRunning() {
        return (status == STATUS.RUNNING);
    }

    public ArrayList<CompactTime> getLaptimes() {
        return laptimes;
    }


    /**
     * todo:=> Thing about how not to make this clash with the main stopWatch
     *
     * @param runTime
     */
    public void runForTime(long runTime) {
        start();
        while (System.currentTimeMillis() - resumeStartTime < runTime) {
            assert true;
        }
        System.out.println(new CompactTime(System.currentTimeMillis() - resumeStartTime));
    }

    public void printClock() {
        System.out.println(mainTime);
    }

    public long getHours() {
        return mainTime.getHours();
    }

    public long getMinutes() {
        return mainTime.getMinutes();
    }

    public long getSeconds() {
        return mainTime.getSeconds();
    }

    public long getMilliSeconds() {
        return mainTime.getMilliSeconds();
    }

    @Override
    public String toString() {
        return "SimpleStopWatchTimer{" +
                "status=" + status +
                ", mainTime=" + mainTime +
                ", baseStartTime=" + baseStartTime +
                ", resumeStartTime=" + resumeStartTime +
                ", pauseStartTime=" + pauseStartTime +
                ", pauseTimeAccumulator=" + pauseTimeAccumulator +
                ", timeAccumulator=" + timeAccumulator +
                ", laptimes=" + laptimes +
                '}';
    }

    public class CompactTime {

        private long hours;
        private int minutes;
        private int seconds;
        private int milliSeconds;
        private long timeInMilliSeconds;

        static final long SECOND_MILLISECOND = 1000;
        static final long MINUTE_MILLISECOND = 60 * SECOND_MILLISECOND;
        static final long HOUR_MILLISECOND = 60 * MINUTE_MILLISECOND;
        static final long DAY_MILLISECOND = 24 * HOUR_MILLISECOND;

        public CompactTime() {
            zeroTime();
        }

        public CompactTime(long timeInMilliSeconds) {
            this();
            setTimeInMilliSeconds(timeInMilliSeconds);
            updateFromMilliSecond();
        }

        public CompactTime(CompactTime compactTime) {
            setTime(compactTime.hours, compactTime.minutes, compactTime.seconds, compactTime.milliSeconds);
        }

        public CompactTime(Date date) {
            setTimeInMilliSeconds(date.getTime());
        }

        public CompactTime(int hours, int minutes, int seconds) {
            this();
            setHours(hours);
            setMinutes(minutes);
            setSeconds(seconds);
            updateToMilliSeconds();
        }

        public CompactTime(long hours, int minutes, int seconds, int milliSeconds) {
            this();
            setTime(hours, minutes, seconds, milliSeconds);
        }

        public void zeroTime() {
            timeInMilliSeconds = 0;
            updateFromMilliSecond();
        }

        private void updateFromMilliSecond() {
            long timeDifference = getTimeInMilliSeconds();

            hours = (timeDifference / HOUR_MILLISECOND);
            timeDifference -= (hours * HOUR_MILLISECOND);

            minutes = (int) (timeDifference / MINUTE_MILLISECOND);
            timeDifference -= (minutes * MINUTE_MILLISECOND);

            seconds = (int) (timeDifference / SECOND_MILLISECOND);
            timeDifference -= (seconds * SECOND_MILLISECOND);

            milliSeconds = (int) timeDifference;
            timeDifference -= milliSeconds;
            assert (timeDifference == 0);

        }

        private void updateToMilliSeconds() {
            long milliAccumulator = 0;
            milliAccumulator += (hours * HOUR_MILLISECOND);
            milliAccumulator += (minutes * MINUTE_MILLISECOND);
            milliAccumulator += (seconds * SECOND_MILLISECOND);
            milliAccumulator += (milliSeconds);
            timeInMilliSeconds = milliAccumulator;
        }

        public void setTime(long hours, int minutes, int seconds, int milliSeconds) {
            setHours(hours);
            setMinutes(minutes);
            setSeconds(seconds);
            setMilliSeconds(milliSeconds);
            updateToMilliSeconds();
        }

        public long getHours() {
            return hours;
        }

        public void setHours(long hours) {
            String hourErrorMessage = "Invalid Hours:- Hours must be positive, ";
            if (hours >= 0)
                this.hours = hours;
            else
                throw new IllegalArgumentException(String.format(Locale.ENGLISH, hourErrorMessage + "(%d)", hours));

        }

        public int getMinutes() {
            return minutes;
        }

        public void setMinutes(int minutes) {
            String minuteErrorMessage = "Invalid Minutes:- Minutes must be positive and less than 60, ";
            if (minutes >= 0 && minutes < 60)
                this.minutes = minutes;
            else
                throw new IllegalArgumentException(String.format(Locale.ENGLISH, minuteErrorMessage + "(%d)", minutes));
        }

        public int getSeconds() {
            return seconds;
        }

        public void setSeconds(int seconds) {
            String secondErrorMessage = "Invalid Seconds:- Minutes must be positive and less than 60, ";
            if (seconds >= 0 && seconds < 60)
                this.seconds = seconds;
            else
                throw new IllegalArgumentException(String.format(Locale.ENGLISH, secondErrorMessage + "(%d)", seconds));
        }

        public int getMilliSeconds() {
            return milliSeconds;
        }

        public void setMilliSeconds(int milliSeconds) {
            String millisecondErrorMessage = "Invalid Milliseconds:- Minutes must be positive and less than 1000, ";
            if (milliSeconds >= 0 && milliSeconds < 1000)
                this.milliSeconds = milliSeconds;
            else
                throw new IllegalArgumentException(String.format(Locale.ENGLISH, millisecondErrorMessage + "(%d)", milliSeconds));
        }

        public long getTimeInMilliSeconds() {
            return timeInMilliSeconds;
        }

        public void setTimeInMilliSeconds(long timeInMilliSeconds) {
            String timeInMilliSecondError = "Invalid Time in Milliseconds:- Hours must be positive, ";
            if (timeInMilliSeconds >= 0) {
                this.timeInMilliSeconds = timeInMilliSeconds;
                updateFromMilliSecond();
            } else
                throw new IllegalArgumentException(String.format(Locale.ENGLISH, timeInMilliSecondError + "(%d)", timeInMilliSeconds));
        }

        @Override
        public String toString() {
            if (hours < 10)
                return String.format(Locale.ENGLISH, "%02d:%02d:%02d.%03d", hours, minutes, seconds, milliSeconds);
            else
                return String.format(Locale.ENGLISH, "%d:%02d:%02d.%03d", hours, minutes, seconds, milliSeconds);
        }

        public String toMilliSecondString() {
            return String.format(Locale.ENGLISH, "%d", timeInMilliSeconds);
        }

        public String toDayTimeString() {
            long timeDifference = getTimeInMilliSeconds();
            long days = timeDifference / DAY_MILLISECOND;
            timeDifference -= (days * DAY_MILLISECOND);

            long dayHours = timeDifference / HOUR_MILLISECOND;
            timeDifference -= (dayHours * HOUR_MILLISECOND);

            long dayMinutes = (int) (timeDifference / MINUTE_MILLISECOND);
            timeDifference -= (minutes * MINUTE_MILLISECOND);

            long daySeconds = (int) (timeDifference / SECOND_MILLISECOND);
            timeDifference -= (seconds * SECOND_MILLISECOND);

            long dayMilliSeconds = (int) timeDifference;
            timeDifference -= milliSeconds;
            assert (timeDifference == 0);


            return String.format(Locale.ENGLISH, "Day: %d, Time(GMT): %02d:%02d:%02d.%03d",
                    days, dayHours, dayMinutes, daySeconds, dayMilliSeconds);
        }

    }
}