package game.image;

import java.util.ArrayList;
import java.util.Collections;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class ImageProcessing extends PApplet {

    PImage img;
    QuadGraph QG;
    PImage houghImg;


    public void setup() {
        size(1800, 400);
        img = loadImage("data/lego_boards/board4.jpg");
        QG = new QuadGraph(this);

        //noLoop(); // no interactive behaviour: draw() will be called only once.


		/*
		 *
		 *
		 * COMMENT ABOUT QUADGRAPH :
		 * 		If I modify the minVotes values in hough to 100 and nLines=6, we get 2 possible 'lego board'... In that case, which one to choose ??
		 * 		Since the build method from QuadGraph should return 'lines' that we send after to getIntesection...
		 *
		 * ASK ABOUT GAUSSIANBLUR :
		 * 		It's the weight value set to 99 (sum of all) good ?
		 *
		 */
    }

    public void draw() {

        background(color(0,0,0));

        img.resize(600, 400);

        image(img,0,0);
        PImage hbsInter = hsbThreshold(img, 80f, 140f, 30f, 200f, 82f);
        PImage blur = gaussianBlur(hbsInter);
        PImage thresholdInter = thresholdBinary(110, blur);
        PImage sobel = sobel(thresholdInter);
        ArrayList<PVector> lines = hough(sobel, 6);

        QG.build(lines, width, img.height);
        getIntersection(lines);


        image(houghImg,600,0);
        image(sobel,1200,0);
    }


    private PImage thresholdBinary(float threshold, PImage img) {
        PImage result = createImage(width, height, RGB);

        for(int i=0; i < img.width ; i++){
            for(int j=0; j < img.height ; j++){

                if (brightness(img.get(i,j)) > threshold) {
                    result.set(i, j, color(255));
                }
                else {
                    result.set(i, j, color(0));
                }
            }
        }

        return result;
    }


    private PImage hsbThreshold(PImage img, float hueMin, float hueMax, float bMin, float bMax, float sMin){

        PImage result = createImage(img.width, img.height, RGB);

        for(int i=0; i < img.width ; i++){
            for(int j=0; j < img.height ; j++){

                float h = hue(img.get(i,j));
                float b = brightness(img.get(i,j));
                float s = saturation(img.get(i,j));

                if(h > hueMin &&  h < hueMax  &&  b > bMin  &&  b < bMax  &&  s > sMin) {
                    result.set(i,j, color(255));
                } else {
                    result.set(i,j, color(0));
                }
            }
        }

        return result;
    }



    public PImage gaussianBlur(PImage img) {

        float[][] kernel = { { 9, 12, 9 },
                { 12, 15, 12 },
                { 9, 12, 9 }};

        float weight = 99.0f;//for blur

        PImage result = createImage(img.width, img.height, RGB);

        for(int i= 1; i < img.width-1; i++){
            for (int j = 1; j<img.height-1; j++){

                float intensities = 0;
                for(int x = i-1; x < i+1 ; x++){
                    for(int y = j-1; y < j+1 ; y++){

                        intensities += brightness(img.get(x,y)) * kernel[x-i+1][y-j+1];
                    }
                }

                result.pixels[j*img.width + i] = (int) (intensities/weight);
            }
        }


        return result;
    }





    public PImage sobel(PImage img) {

        float[][] hKernel = { { 0, 1, 0 },
                { 0, 0, 0 },
                { 0, -1, 0 } };

        float[][] vKernel = { { 0, 0, 0 },
                { 1, 0, -1 },
                { 0, 0, 0 } };


        PImage result = createImage(img.width, img.height, ALPHA);

        // clear the image
        for (int i = 0; i < img.width * img.height; i++) {
            result.pixels[i] = color(0);
        }

        float max=0;
        float[] buffer = new float[img.width * img.height];


        for(int i= 1; i < img.width-1; i++){
            for (int j = 1; j<img.height-1; j++){

                int sum_h = 0;
                int sum_v = 0;

                for(int x = i-1; x < i+2 ; x++){
                    for(int y = j-1; y < j+2 ; y++){
                        sum_h += img.get(x,y) * hKernel[x-i+1][y-j+1];
                        sum_v += img.get(x,y) * vKernel[x-i+1][y-j+1];
                    }
                }

                float sum = sqrt(pow(sum_h,2) + pow(sum_v,2));
                buffer[j*img.width + i] = sum;

                if(sum > max){
                    max = sum;
                }
            }
        }


        for (int y = 2; y < img.height - 2; y++) { // Skip top and bottom edges
            for (int x = 2; x < img.width - 2; x++) { // Skip left and right

                if (buffer[y * img.width + x] > (int)(max * 0.3f)) { // 30% of the max
                    result.pixels[y * img.width + x] = color(255);
                } else {
                    result.pixels[y * img.width + x] = color(0);
                }
            }
        }

        return result;
    }






    boolean first = true;
    float discretizationStepsPhi = 0.06f;
    float discretizationStepsR = 2.5f;

    // dimensions of the accumulator
    int phiDim = (int) (Math.PI / discretizationStepsPhi);

    float[] tabSin = new float[phiDim];
    float[] tabCos = new float[phiDim];


    public ArrayList<PVector> hough(PImage edgeImg, int nLines) {


        int rDim = (int) (((edgeImg.width + edgeImg.height) * 2 + 1) / discretizationStepsR);

        // our accumulator (with a 1 pix margin around)
        int[] accumulator = new int[(phiDim + 2) * (rDim + 2)];


        if(first){
            float ang = 0;
            //	float inverseR = 1.f / discretizationStepsR;

            for (int accPhi = 0; accPhi < phiDim; ang += discretizationStepsPhi, accPhi++) {
                // we can also pre-multiply by (1/discretizationStepsR) since we need it in the Hough loop
                tabSin[accPhi] = (float) (Math.sin(ang) );
                tabCos[accPhi] = (float) (Math.cos(ang));
            }

            first = false;
        }


        // Fill the accumulator: on edge points (ie, white pixels of the edge
        // image), store all possible (r, phi) pairs describing lines going
        // through the point.
        for (int y = 0; y < edgeImg.height; y++) {
            for (int x = 0; x < edgeImg.width; x++) {
                // Are we on an edge?
                if (brightness(edgeImg.pixels[y * edgeImg.width + x]) != 0) {

                    float phi = 0;
                    for (int i=0; i < phiDim ; i++, phi+= discretizationStepsPhi){

                        float r = x*tabCos[i] + y*tabSin[i];

                        float accPhi =  Math.round(phi / discretizationStepsPhi);
                        float accR =  Math.round((r/discretizationStepsR) + ((rDim-1)/2) );

                        int idx = (int) (accR + 1 + (accPhi+1)*(rDim+2));

                        accumulator[ idx ]++; //increment the accumulator at pos (r,phi)
                    }
                }
            }
        }



//		PRINT THE HOUGH ACCUMULATOR

        houghImg = createImage(rDim + 2, phiDim +2, ALPHA);

        for(int i=0; i< accumulator.length; i++){
            houghImg.pixels[i] = color(min(255, accumulator[i]));
        }

        houghImg.updatePixels();

        houghImg.resize(img.width, img.height);






        //SELECT ONLY THE MOST VOTED NLINES, value bigger thatminVotes
        int minVotes = 200;
        // size of the region we search for a local maximum
        int neighbourhood = 10;

        ArrayList<Integer> bestCandidates = new ArrayList<Integer>();

        for (int accR = 0; accR < rDim; accR++) {
            for (int accPhi = 0; accPhi < phiDim; accPhi++) {
                // compute current index in the accumulator
                int idx = (accPhi + 1) * (rDim + 2) + accR + 1;
                if (accumulator[idx] > minVotes) {
                    boolean bestCandidate=true;
                    // iterate over the neighbourhood
                    for(int dPhi=-neighbourhood/2; dPhi < neighbourhood/2+1; dPhi++) {
                        // check we are not outside the image
                        if( accPhi+dPhi < 0 || accPhi+dPhi >= phiDim) continue;
                        for(int dR=-neighbourhood/2; dR < neighbourhood/2 +1; dR++) {

                            // check we are not outside the image
                            if(accR+dR < 0 || accR+dR >= rDim) continue;
                            int neighbourIdx = (accPhi + dPhi + 1) * (rDim + 2) + accR + dR + 1;
                            if(accumulator[idx] < accumulator[neighbourIdx]) {
                                // the current idx is not a local maximum!
                                bestCandidate=false;
                                break;
                            }
                        }
                        if(!bestCandidate) break;
                    }
                    if(bestCandidate) {
                        // the current idx *is* a local maximum
                        bestCandidates.add(idx);
                    }
                }
            }
        }


        Collections.sort(bestCandidates, new HoughComparator(accumulator));//sort the arrayList
        int[] accumulatorNew = new int[(phiDim + 2) * (rDim + 2)];



        nLines = min(nLines, bestCandidates.size());


        for(int i = 0; i< nLines; i++ ){
            int idx = bestCandidates.get(i) ;
            accumulatorNew[idx] = accumulator[idx];
        }

        ArrayList<PVector> result = new ArrayList<PVector>();



        //PLOT LINES
        for (int idx = 0; idx < accumulatorNew.length; idx++) {
            if (accumulatorNew[idx] > minVotes) {
                // first, compute back the (r, phi) polar coordinates:
                int accPhi = (int) (idx / (rDim + 2)) - 1;
                int accR = idx - (accPhi + 1) * (rDim + 2) - 1;
                float r = (accR - (rDim - 1) * 0.5f) * discretizationStepsR;
                float phi = accPhi * discretizationStepsPhi;

                result.add(new PVector(r,phi));

                int x0 = 0;
                int y0 = (int) (r / sin(phi));
                int x1 = (int) (r / cos(phi));
                int y1 = 0;
                int x2 = edgeImg.width;
                int y2 = (int) (-cos(phi) / sin(phi) * x2 + r / sin(phi));
                int y3 = edgeImg.width;
                int x3 = (int) (-(y3 - r / sin(phi)) * (sin(phi) / cos(phi)));

                // Finally, plot the lines
                stroke(204,102,0);
                if (y0 > 0) {
                    if (x1 > 0)
                        line(x0, y0, x1, y1);
                    else if (y2 > 0)
                        line(x0, y0, x2, y2);
                    else
                        line(x0, y0, x3, y3);
                }
                else {
                    if (x1 > 0) {
                        if (y2 > 0)
                            line(x1, y1, x2, y2);
                        else
                            line(x1, y1, x3, y3);
                    }
                    else
                        line(x2, y2, x3, y3);
                }
            }

        }

        return result;

    }



    public ArrayList<PVector> getIntersection(ArrayList<PVector> lines){

        ArrayList<PVector> intersections = new ArrayList<PVector>();

        for (int i = 0; i < lines.size() - 1; i++) {
            PVector line1 = lines.get(i);
            float r1 = line1.x;
            float phi1 = line1.y;

            for (int j = i + 1; j < lines.size(); j++) {
                PVector line2 = lines.get(j);
                float r2 = line2.x;
                float phi2 = line2.y;

                // compute the intersection and add it to 'intersections'
                float d = cos(phi2)*sin(phi1) - cos(phi1)*sin(phi2);
                float x = ((r2*sin(phi1))-(r1*sin(phi2)))/d;
                float y = ((-1*r2*cos(phi1))+(r1*cos(phi2)))/d;

                PVector pv = new PVector(x,y);
                if( (x >= 0  &&  x <= width) && (y >= 0  &&  y <= height) ){
                    intersections.add(pv);
                }
                // draw the intersection
                fill(255, 128, 0);
                ellipse(x, y, 10, 10);
            }
        }

        return intersections;
    }

    static public void main(String[] args) {
        String[] appletArgs = new String[]{"game.image.ImageProcessing"};
        PApplet.main(appletArgs);
    }


}