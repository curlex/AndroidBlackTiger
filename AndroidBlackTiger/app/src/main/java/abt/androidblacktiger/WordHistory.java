package abt.androidblacktiger;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Maria on 22/10/2015.
 * Words
 * Translation
 * Location
 * #times shown
 * show again
 * image
 */

/**
 * Class for word history object and all the aspects needed to describe it
 */
public class WordHistory  {
    private int _id;
    private String _word;
    private String _lang;
    private String _translation;
    private List<CoOrdinates> _location;
    private int _shown; //number of times shown
    private boolean _again; //if it's to be shown again
    private String _imagePath;


    public WordHistory() {
        this._word = null;
        this._lang = null;
        this._translation = null;
        this._location = null;
        this._shown = 0;
        this._again = true;
        this._imagePath = null;

    }


    /**
     *  Constructor for a list of co-ordinates
     * @param word
     * @param lang
     * @param translation
     * @param location
     * @param shown
     * @param again
     * @param imagePath
     */
    public WordHistory( String word, String lang, String translation, List<CoOrdinates> location, int shown, boolean again, String imagePath) {
        this._word = word;
        this._lang = lang;
        this._translation = translation;
        this._location = location;
        this._shown = shown;
        this._again = again;
        this._imagePath = imagePath;
    }


    /**
     * Constructor for a single coordinate rather than a list
     * @param word
     * @param lang
     * @param translation
     * @param location
     * @param shown
     * @param again
     * @param imagePath
     */
    public WordHistory( String word, String lang, String translation, CoOrdinates location, int shown, boolean again, String imagePath) {
        this._word = word;
        this._lang = lang;
        this._translation = translation;
        List coOrd = new LinkedList<CoOrdinates>();
        coOrd.add(location);
        this._location = coOrd;
        this._shown = shown;
        this._again = again;
        this._imagePath = imagePath;
    }


    /**
     * Below are the getters and setters for the class
     * */
        public void setWord(String word) { this._word = word; }
        public String getWord() {
            return this._word;
        }

        public void setLang(String lang){ this._lang = lang; }
        public String getLang(){ return this._lang;}


        public  void setTranslation(String translation){
            this._translation = translation;
        }
        public String getTranslation() {
            return this._translation;
        }

        public  void setLocations(List<CoOrdinates> location){
            this._location = location;
        }
        public List<CoOrdinates> getLocations() {
            return this._location;
        }

        public void setShown(int quantity) {
            this._shown = quantity;
        }
        public int getShown() {
            return this._shown;
        }

        public void setAgain(boolean again){ this._again = again; }
        public boolean getAgain() {return this._again; }

        public  void setImagePath(String image){
            this._imagePath = image;
        }
        public String getImagePath() {
            return this._imagePath;
        }

    /**
     * Method for printing the details during the debugging
     * @return string of the objects fields
     */
    public String toString(){
        return ""+_word+" "+_lang+" "+_translation+" "+_location+" "+_shown+" "+_again+" "+_imagePath;
    }


}
