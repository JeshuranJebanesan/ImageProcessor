import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class Main {
	public static void main(String[] args) throws IOException {
		String fps = "/Users/jeshuranjebanesan/CS/Java/ImageProcessor/ImageProcessor/resources/images/image.png";
		String fpd = "/Users/jeshuranjebanesan/CS/Java/ImageProcessor/ImageProcessor/resources/images/image2.png";
		
		//read in image
		File source = new File(fps);
		File destination = new File(fpd);
		destination.createNewFile();
		BufferedImage originalImage = ImageIO.read(source);
		
		//process image
		BufferedImage alteredImage = Image.subtractImage(Image.differenceOfGaussians(originalImage), originalImage);
		//BufferedImage alter1 = Image.addImage(Image.edgeDetection(originalImage, 200), originalImage);
		//BufferedImage alter2 = Image.addImage(Image.differenceOfGaussians(originalImage), originalImage);
		
		//write image to file
		//ImageIO.write(alteredImage, "png", destination);
		Image.display(originalImage);
		Image.display(alteredImage);
		//Image.display(alter1);
		//Image.display(alter2);
	}
}