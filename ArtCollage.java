/*************************************************************************
 *  Compilation:  javac ArtCollage.java
 *  Execution:    java ArtCollage Flo2.jpeg
 *
 *  @author:
 *
 *************************************************************************/

import java.awt.Color;

public class ArtCollage {

    // The orginal picture
    private Picture original;

    // The collage picture
    private Picture collage;

    // The collage Picture consists of collageDimension X collageDimension tiles
    private int collageDimension;

    // A tile consists of tileDimension X tileDimension pixels
    private int tileDimension;
    
    /*
     * One-argument Constructor
     * 1. set default values of collageDimension to 4 and tileDimension to 100
     * 2. initializes original with the filename image
     * 3. initializes collage as a Picture of tileDimension*collageDimension x tileDimension*collageDimension, 
     *    where each pixel is black (see all constructors for the Picture class).
     * 4. update collage to be a scaled version of original (see scaling filter on Week 9 slides)
     *
     * @param filename the image filename
     */
    public ArtCollage (String filename) {

        // WRITE YOUR CODE HERE
        //sets default values to 4 and 100
        collageDimension = 4;
        tileDimension = 100;
        //initializes original with filename image
        original = new Picture(filename);
        //initializes collage as picture of collageDimension*tileDimension x collageDimension*tileDimension
        collage = new Picture(tileDimension*collageDimension,tileDimension*collageDimension);
        //updates collage to scaled version
        for (int i=0; i<400 ;i++) {
            for (int j=0; j<400; j++) {
                int scaledwidth = i*(original.width()/400);
                int scaledheight = j*(original.height()/400);
                Color color = original.get(scaledwidth,scaledheight);
                collage.set(i,j,color);
            }
        }
    }

    /*
     * Three-arguments Constructor
     * 1. set default values of collageDimension to cd and tileDimension to td
     * 2. initializes original with the filename image
     * 3. initializes collage as a Picture of tileDimension*collageDimension x tileDimension*collageDimension, 
     *    where each pixel is black (see all constructors for the Picture class).
     * 4. update collage to be a scaled version of original (see scaling filter on Week 9 slides)
     *
     * @param filename the image filename
     */
    public ArtCollage (String filename, int td, int cd) {

        // WRITE YOUR CODE HERE
        //sets default values to cd and td
        collageDimension = cd;
        tileDimension = td;
        //intializes original with filename image
        original = new Picture (filename);
        //initializes collage as picture of collageDimension*tileDimension x collageDimension*tileDimension
        int wh = cd*td;
        collage = new Picture(wh,wh);
        //update collage to scalred version
        for (int i=0; i<wh ;i++) {
            for (int j=0; j<wh; j++) {
                int scaledwidth = i*(original.width()/wh);
                int scaledheight = j*(original.height()/wh);
                Color color = original.get(scaledwidth,scaledheight);
                collage.set(i,j,color);

    }
}
    }

    /*
     * Returns the collageDimension instance variable
     *
     * @return collageDimension
     */
    public int getCollageDimension() {

        // WRITE YOUR CODE HERE
        return collageDimension;
    }

    /*
     * Returns the tileDimension instance variable
     *
     * @return tileDimension
     */
    public int getTileDimension() {

        // WRITE YOUR CODE HERE
        return tileDimension;
    }

    /*
     * Returns original instance variable
     *
     * @return original
     */
    public Picture getOriginalPicture() {

        // WRITE YOUR CODE HERE
        return original;
    }

    /*
     * Returns collage instance variable
     *
     * @return collage
     */
    public Picture getCollagePicture() {

        // WRITE YOUR CODE HERE
        return collage;
    }
    
    /*
     * Display the original image
     * Assumes that original has been initialized
     */
    public void showOriginalPicture() {

        // WRITE YOUR CODE HERE
        original.show();
    }

    /*
     * Display the collage image
     * Assumes that collage has been initialized
     */
    public void showCollagePicture() {

        // WRITE YOUR CODE HERE
        collage.show();
    }

    /*
     * Replaces the tile at collageCol,collageRow with the image from filename
     * Tile (0,0) is the upper leftmost tile
     *
     * @param filename image to replace tile
     * @param collageCol tile column
     * @param collageRow tile row
     */
    public void replaceTile (String filename,  int collageCol, int collageRow) {

        // WRITE YOUR CODE HERE
        Picture before = new Picture(filename);
        int t = this.tileDimension;
        Picture after = new Picture (t, t);
        for (int column = 0; column < t; column++){
            for (int row = 0; row < t; row++){
                int columnNew = column * before.width() / t;
                int rowNew = row * before.height() / t;
                Color color = before.get(columnNew,rowNew);
                after.set(column, row, color);
            }
        }
        before = after;
        int x = this.collageDimension;
        for(int c = 0; c < t; c++) {
            for(int r = 0; r < t; r++) {
                Color color = before.get(c,r);
                for (int i = collageCol; i < collageCol + 1; i++) {
                    for (int j = collageRow; j < collageRow + 1; j++) {
                        this.collage.set(t*i + c, t*j + r, color);
                    }
                }
            }
        }
        
    }
    
    /*
     * Makes a collage of tiles from original Picture
     * original will have collageDimension x collageDimension tiles, each tile
     * has tileDimension X tileDimension pixels
     */
    public void makeCollage () {

        // WRITE YOUR CODE HERE
        int width = this.tileDimension;
        int height = this.tileDimension;
        Picture scale = new Picture (width,height);
        for  (int column = 0; column<this.tileDimension; column++) {
            for (int row = 0; row<this.tileDimension; row++) {
                        int columnNew = column*(this.original.width()/width);
                        int rowNew = row*(this.original.height()/height);
                        Color color = original.get(columnNew,rowNew);
                        scale.set(column,row,color);
            }
        }
        collage = new Picture(this.collageDimension * scale.width(), this.collageDimension * scale.height());
        for (int c = 0; c < width; c++) {
            for (int r = 0; r < height; r++) {
                Color color = scale.get(c, r);
                for (int i = 0; i < this.collageDimension; i++) {
                    for (int j = 0; j < this.collageDimension; j++) {
                        this.collage.set(height*j + c, width*i + r, color);
                    }
                }
            }
        }
    }


    /*
     * Colorizes the tile at (collageCol, collageRow) with component 
     * (see CS111 Week 9 slides, the code for color separation is at the 
     *  book's website)
     *
     * @param component is either red, blue or green
     * @param collageCol tile column
     * @param collageRow tile row
     */
    public void colorizeTile (String component,  int collageCol, int collageRow) {

        // WRITE YOUR CODE HERE
        Picture original = getCollagePicture();
        int tile = getTileDimension();
        for (int col = collageCol*tile; col<tileDimension; col++) {
            for (int row = collageRow*tile; row<(tileDimension); row++) {
                    Color color = original.get(col,row);
                    int temp = 0;
                            if (component.equals("red")) {
                                temp = color.getRed();
                                original.set(col, row, new Color(temp,0,0));
                            }
                            else if (component.equals("blue")) {
                                temp = color.getBlue();
                                original.set(col, row, new Color(0,0,temp));
                            }
                            else if (component.equals("green")) {
                                temp = color.getGreen();
                                original.set(col, row, new Color(0,temp,0));
                            }
                        }
                    }
                }

    /*
     * Grayscale tile at (collageCol, collageRow)
     * (see CS111 Week 9 slides, the code for luminance is at the book's website)
     *
     * @param collageCol tile column
     * @param collageRow tile row
     */

    public void grayscaleTile (int collageCol, int collageRow) {

        // WRITE YOUR CODE HERE
        //access collage, pick out the tile from that
        Picture original = getCollagePicture();
        int tile = getTileDimension();
        for (int col = collageCol*tile; col<(tile+(collageCol*tile)); col++) {
            for (int row = 0; row<(tile+(collageRow*tile)); row++) {
                    Color color = original.get(col,row);
                    Color gray = Luminance.toGray(color);
                    original.set(col,row,gray);
                }  
            }
            collage = original;
        }
    


    /*
     *
     *  Test client: use the examples given on the assignment description to test your ArtCollage
     */
    public static void main (String[] args) {

        ArtCollage art = 
  new ArtCollage(args[0], 200, 3);

art.makeCollage();

// Colorize tile at col 2, row 2 
// to only show the red component
art.colorizeTile("red",2,2);
art.showCollagePicture();
    }
}
