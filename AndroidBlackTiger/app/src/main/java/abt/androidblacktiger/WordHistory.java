package abt.androidblacktiger;

/**
 * Created by Maria on 22/10/2015.
 * Words
 * Translation
 * Location
 * #times shown
 * show again
 * image
 */
public class WordHistory  {
    private int _id;
    private String _word;
    private String _translation;
    private String _location;
    private int _shown;
    private String _imagePath;

        public WordHistory() {

        }

        public WordHistory(int id, String word, String translation, String location, int shown, String imagePath) {
            this._id =id;
            this._word = word;
            this._translation = translation;
            this._location = location;
            this._shown = shown;
            this._imagePath = imagePath;
        }

        public WordHistory( String word, String translation, String location, int shown, String imagePath) {
            this._word = word;
            this._translation = translation;
            this._location = location;
            this._shown = shown;
            this._imagePath = imagePath;
        }

        public void setID(int id) {
            this._id = id;
        }

        public int getID() {
            return this._id;
        }

        public void setWord(String word) {
            this._word = word;
        }

        public String getWord() {
            return this._word;
        }

        public  void setTranslation(String translation){
            this._translation = translation;
        }

        public String getTranslation() {
            return this._translation;
        }

        public  void setLocation(String location){
            this._location = location;
        }

        public String getLocation() {
            return this._location;
        }

        public void setShown(int quantity) {
            this._shown = quantity;
        }

        public int getShown() {
            return this._shown;
        }

        public  void setImagePath(String image){
            this._imagePath = image;
        }

        public String getImagePath() {
            return this._imagePath;
        }

        public String toString(){
            return ""+_id+" "+_word+" "+_translation+" "+_shown+" "+_location+" "+_imagePath;
        }


}
