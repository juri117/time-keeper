import javax.swing.*;

class Uhr extends Thread {
    //public void main ()    //String[] args
    //throws IOException
    //{

    private Ziffern h = new Ziffern(24);
    private Ziffern m = new Ziffern(60);
    private Ziffern s = new Ziffern(60);
    private Boolean runs = false;
    private JTextField time;
    private Boolean useTimeStamp = false;
    private long currentTime;

    /*
     public void Uhr (h, m, s) {
     this.h = h;
     this.m = m;
     this.s = s;
     }
     */
    public Uhr(int h, int m, int s, Boolean useTimeStamp, JTextField time) {
        this.time = time;
        setTime(h, m, s);
        currentTime = System.currentTimeMillis();
        gibUhrzeit();
    }

    public int setTime(int h, int m, int s) {
        this.h.setStart(h);
        this.m.setStart(m);
        this.s.setStart(s);
        currentTime = System.currentTimeMillis();
        gibUhrzeit();
        return 0;
    }

    public void setUseTimeStamp(Boolean useIt) {
        useTimeStamp = useIt;
    }

    public void run() {
        while (isInterrupted() == false) {
            if (runs && !useTimeStamp) {
                taktsignalGeben(stunden(), minuten(), sekunden());
                gibUhrzeit();
            }
            delay();
        }
    }

    public void stopIt() {
        if (useTimeStamp) {
            long timePassed = (System.currentTimeMillis() - currentTime);
            timePassed += (stunden() * 60 * 60 + minuten() * 60 + sekunden()) * 1000;
            int seconds = (int) (timePassed / 1000) % 60;
            int minutes = (int) ((timePassed / (1000 * 60)) % 60);
            int hours = (int) ((timePassed / (1000 * 60 * 60)));
            setTime(hours, minutes, seconds);
        }
        runs = false;
    }

    public void startIt() {
        currentTime = System.currentTimeMillis();
        String[] timeAry = time.getText().split(":");
        setTime(Integer.parseInt(timeAry[0]), Integer.parseInt(timeAry[1]), Integer.parseInt(timeAry[2]));
        runs = true;
    }

    public int stunden() {
        return h.getZiffer();
    }

    public int minuten() {
        return m.getZiffer();
    }

    public int sekunden() {
        return s.getZiffer();
    }

    public void delay() {
        try {
            Thread.sleep(1000L);
        } catch (Exception e) {
            System.out.println("error @ waiting");
        }
    }

    public String gibUhrzeit() {
        String hour;
        String min;
        String sec;
        if (stunden() < 10) {
            hour = "0" + stunden();
        } else {
            hour = "" + stunden();
        }
        if (minuten() < 10) {
            min = "0" + minuten();
        } else {
            min = "" + minuten();
        }
        if (sekunden() < 10) {
            sec = "0" + sekunden();
        } else {
            sec = "" + sekunden();
        }
        time.setText(hour + ":" + min + ":" + sec);
        //System.out.println (hour + ":" + min + ":" + sec);
        return hour + ":" + min + ":" + sec;
    }

    public int taktsignalGeben(int h, int m, int s) {
        if (this.s.erhoehe() == 0) {
            if (this.m.erhoehe() == 0) {
                if (this.h.erhoehe() == 0) {
                    return 0;
                }
            } else {
                return 0;
            }
        } else {
            return 0;
        }
        return 0;
    }
}  //Ende Klasse Eingabe

