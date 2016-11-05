package android518.qwnasfirebasequotes;


import java.io.Serializable;

/**
 * @author William Ngo
 *         Alena Shulzhenko
 *
 * @version 2016-11-04
 */

public class Quote implements Serializable {
    private String attributed;
    private String blurb;
    private String quote;
    private String reference;
    private String date; //no support for Java8, maybe use java.util.Date
    //private String category; according to specs we don't need this?

    public Quote (String attributed, String blurb, String quote, String reference,
                  String date){//, String category) {
        this.attributed = attributed;
        this.blurb = blurb;
        this.quote = quote;
        this.reference = reference;
        this.date = date;
        //this.category = category;
    }

    //Getters
    public String getAttributed()
    {
        return this.attributed;
    }
    public String getBlurb()
    {
        return this.blurb;
    }
    public String getQuote()
    {
        return this.quote;
    }
    public String getReference()
    {
        return this.reference;
    }
    public String getDate() { return this.date; }


    public String toString() {
        return "attributed: " + attributed + "; blurb: " + blurb + "; quote: " + quote +
                "; reference: " + reference + "; date: " + date;
    }
}
