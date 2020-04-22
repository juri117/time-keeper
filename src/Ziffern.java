public class Ziffern //(String[] args)
{

    int ziffer;
    int max;

    public Ziffern(int max) {
        this.max = max;
    }

    public int setStart(int start) {
        if (ziffer < max) {
            ziffer = start;
        } else {
            ziffer = 0;
        }
        return ziffer;
    }

    public int getZiffer() {
        return ziffer;
    }

    public int erhoehe() {
        ziffer = ziffer + 1;
        if (ziffer >= max) {
            ziffer = 0;
        }
        return ziffer;
    }
}  //Ende Klasse Eingabe

