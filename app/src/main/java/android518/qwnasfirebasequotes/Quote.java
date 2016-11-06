package android518.qwnasfirebasequotes;


import java.io.Serializable;

/**
 * Class that represents the Quote object.
 *
 * @author William Ngo
 * @author Alena Shulzhenko
 * @version 2016-11-04
 */
public class Quote implements Serializable {
    private String attributed;
    private String blurb;
    private String quote;
    private String reference;
    private String date;

    /**
     * Instantiates the Quote object with the necessary values.
     * @param attributed the author of the quote.
     * @param blurb the information about the author of the quote.
     * @param quote the quote.
     * @param reference the reference to the website where this quote is from.
     * @param date the date where this quote was added to the database.
     */
    public Quote (String attributed, String blurb, String quote, String reference,
                  String date) {
        this.attributed = attributed;
        this.blurb = blurb;
        this.quote = quote;
        this.reference = reference;
        this.date = date;
    }

    //Getters
    /**
     * Returns the author of the quote.
     * @return the author of the quote.
     */
    public String getAttributed() {
        return this.attributed;
    }

    /**
     * Returns the information about the author of the quote.
     * @return the information about the author of the quote.
     */
    public String getBlurb()
    {
        return this.blurb;
    }

    /**
     * Returns the quote.
     * @return the quote
     */
    public String getQuote()
    {
        return this.quote;
    }

    /**
     * Returns the reference to the website where this quote is from.
     * @return the reference to the website where this quote is from.
     */
    public String getReference()
    {
        return this.reference;
    }

    /**
     * Returns the date where this quote was added to the database.
     * @return the date where this quote was added to the database.
     */
    public String getDate() {
        return this.date;
    }

    /**
     * Returns the string representation of the Quote object.
     * @return the string representation of the Quote object.
     */
    public String toString() {
        return "attributed: " + attributed + "; blurb: " + blurb + "; quote: " + quote +
                "; reference: " + reference + "; date: " + date;
    }
}
