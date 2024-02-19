import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class Image { 

	public static void display(BufferedImage image) {
		JFrame frame = new JFrame();
		JLabel label = new JLabel();
		frame.setSize(image.getWidth(), image.getHeight());
		label.setIcon(new ImageIcon(image));
		frame.getContentPane().add(label, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static float normalise(int x) {
		float normal;

		normal = (float)(x)/255;
		return normal;
	}

	public static int denormalise(float x) {
		int denormal;
		
		denormal = (int)(x*255);
		return denormal;
	}
	
	public static double[][] gaussianMatrix(int dimension, int sigma) {
		double[][] matrix = new double[dimension][dimension];
		double sum = 0;
		double exponent;
		
		for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
            	double x = i - (dimension - 1) / 2.0; // Center the coordinates around the middle of the matrix
                double y = j - (dimension - 1) / 2.0;
                exponent = -(x * x + y * y) / (2 * sigma * sigma);
                matrix[i][j] = Math.exp(exponent) / (2 * Math.PI * sigma * sigma);
                sum += matrix[i][j];
            }
        }
		
		for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                matrix[i][j] /= sum;
            }
        }	
		
		return matrix;
	}
	
 	public static BufferedImage grayscale(BufferedImage originalImage) {
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();
		Color originalColor, alteredColor;
		int gray;
		BufferedImage alteredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				originalColor = new Color(originalImage.getRGB(i, j));
				gray = (int)(0.299*originalColor.getRed() + 0.587*originalColor.getGreen() + 0.114*originalColor.getBlue());
				alteredColor = new Color(gray, gray, gray);
				alteredImage.setRGB(i, j, alteredColor.getRGB());
			}
		}
		return alteredImage;
	}
	
	public static BufferedImage nLinearPixellate(BufferedImage originalImage, int pixels) {
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();
		Color originalColor, alteredColor;
		int red, blue, green;
		BufferedImage alteredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int i = 0; i < width-pixels; i += pixels) {
			for(int j = 0; j < height-pixels; j += pixels) {
				red = blue = green = 0;
				for(int x = 0; x < pixels; x++) {
					for(int y = 0; y < pixels; y++) {
						originalColor = new Color(originalImage.getRGB(i+x, j+y));
						red += originalColor.getRed();
						blue += originalColor.getBlue();
						green += originalColor.getGreen();
					}
				}
				alteredColor = new Color((int)(red/(pixels*pixels)), (int)(green/(pixels*pixels)), (int)(blue/(pixels*pixels)));
				for(int x = 0; x < pixels; x++) {
					for(int y = 0; y < pixels; y++) {
						alteredImage.setRGB(i+x, j+y, alteredColor.getRGB());
					}
				}
			}
		}
		return alteredImage;
	}

	public static BufferedImage pointFilterPixellate(BufferedImage originalImage, int pixels) {
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();
		Color alteredColor;
		BufferedImage alteredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int i = 0; i < width-pixels; i += pixels) {
			for(int j = 0; j < height-pixels; j += pixels) {
				alteredColor = new Color(originalImage.getRGB((int)(i+pixels/2), (int)(j+pixels/2)));
				for(int x = 0; x < pixels; x++) {
					for(int y = 0; y < pixels; y++) {
						alteredImage.setRGB(i+x, j+y, alteredColor.getRGB());
					}
				}
			}
		}
		return alteredImage;
	}
	
	public static BufferedImage addBrighten(BufferedImage originalImage, int brighten) {
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();
		Color originalColor, alteredColor;
		BufferedImage alteredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				originalColor = new Color(originalImage.getRGB(i, j));
				alteredColor = new Color(Math.min(originalColor.getRed()+brighten, 255), Math.min(originalColor.getGreen()+brighten, 255), Math.min(originalColor.getBlue()+brighten, 255));
				alteredImage.setRGB(i, j, alteredColor.getRGB());
			}
		}
		return alteredImage;
	}
	
	public static BufferedImage subtractDarken(BufferedImage originalImage, int darken) {
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();
		Color originalColor, alteredColor;
		BufferedImage alteredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				originalColor = new Color(originalImage.getRGB(i, j));
				alteredColor = new Color(Math.max(originalColor.getRed()-darken, 0), Math.max(originalColor.getGreen()-darken, 0), Math.max(originalColor.getBlue()-darken, 0));
				alteredImage.setRGB(i, j, alteredColor.getRGB());
			}
		}
		return alteredImage;
	}

	public static BufferedImage multiplyDarken(BufferedImage originalImage, float multiply) {
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();
		Color originalColor, alteredColor;
		float red, blue, green;
		BufferedImage alteredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				originalColor = new Color(originalImage.getRGB(i, j));
				red = normalise(originalColor.getRed());
				green = normalise(originalColor.getGreen());
				blue = normalise(originalColor.getBlue());
				alteredColor = new Color(denormalise(red*multiply), denormalise(green*multiply), denormalise(blue*multiply));
				alteredImage.setRGB(i, j, alteredColor.getRGB());
			}
		}
		return alteredImage;
	}
	
	public static BufferedImage gaussianBlur(BufferedImage originalImage, int dimension, int sigma) {
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();
		Color originalColor, alteredColor;
		double red, blue, green;
		double[][] matrix = gaussianMatrix(dimension, sigma);
		BufferedImage alteredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int i = (dimension-1)/2; i < width-(dimension-1)/2; i++) {
			for(int j = (dimension-1)/2; j < height-(dimension-1)/2; j++) {
				red = blue = green = 0;
				for(int x = (dimension-1)/-2; x < (dimension-1)/2; x++) {
					for(int y = (dimension-1)/-2; y < (dimension-1)/2; y++) {
						originalColor = new Color(originalImage.getRGB(i+x, j+y));
						red += matrix[x+(dimension-1)/2][y+(dimension-1)/2]*originalColor.getRed();
						green += matrix[x+(dimension-1)/2][y+(dimension-1)/2]*originalColor.getGreen();
						blue += matrix[x+(dimension-1)/2][y+(dimension-1)/2]*originalColor.getBlue();
					}
				}
				alteredColor = new Color((int)(red), (int)(green), (int)(blue));
				alteredImage.setRGB(i, j, alteredColor.getRGB());
			}
		}
		return alteredImage;
	}

	public static BufferedImage edgeDetection(BufferedImage originalImage, int threshold) {
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();
		int value;
		Color alteredColor;
		double[][] vertical = {{1, 0, -1}, {2, 0, -2}, {1, 0, -1}};
		double[][] horizontal = {{1, 2, 1}, {0, 0, 0}, {-1, -2, -1}};
		int[][] vert = new int[width][height];
		int[][] hor = new int[width][height];
		int[][] edge = new int[width][height];
		BufferedImage alteredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int i = 1; i < width-1; i++) {
			for(int j = 1; j < height-1; j++) {
				vert[i][j] = hor[i][j] = edge[i][j] = 0;
				for(int x = -1; x < 2; x++) {
					for(int y = -1; y < 2; y++) {
						value = new Color(originalImage.getRGB(i+x, j+y)).getRed();
						vert[i][j] += vertical[x+1][y+1]*value;
						hor[i][j] += horizontal[x+1][y+1]*value;
					}
				}
				edge[i][j] = (int)(Math.sqrt(vert[i][j] * vert[i][j] + hor[i][j] * hor[i][j]));
				if(edge[i][j] > threshold) {
					alteredColor = new Color(255, 255, 255);
				} else {
					alteredColor = new Color(0, 0, 0);
				}
				alteredImage.setRGB(i, j, alteredColor.getRGB());
			}
		}
		return alteredImage;
	}

	public static BufferedImage subtractImage(BufferedImage image1, BufferedImage image2) {
		int width = image1.getWidth();
		int height = image2.getHeight();
		Color color1, color2, result;
		int r1, r2, g1, g2, b1, b2, r, g, b;
		BufferedImage alteredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				color1 = new Color(image1.getRGB(i, j));
				color2 = new Color(image2.getRGB(i, j));
				r1 = color1.getRed();
				r2 = color2.getRed();
				g1 = color1.getGreen();
				g2 = color2.getGreen();
				b1 = color1.getBlue();
				b2 = color2.getBlue();
				r = Math.abs(r1-r2);
				g = Math.abs(g1-g2);
				b = Math.abs(b1-b2);
				result = new Color(r, g, b);
				alteredImage.setRGB(i, j, result.getRGB());
			}
		}
		return alteredImage;
	}

	public static BufferedImage differenceOfGaussians(BufferedImage originalImage) {
		BufferedImage alteredImage = subtractImage(grayscale(gaussianBlur(originalImage, 13, 1)), grayscale(gaussianBlur(originalImage, 13, 5)));
		return alteredImage;
	}

	public static BufferedImage addImage(BufferedImage image1, BufferedImage image2) {
		int width = image1.getWidth();
		int height = image2.getHeight();
		Color color1, color2, result;
		int r1, r2, g1, g2, b1, b2, r, g, b;
		BufferedImage alteredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				color1 = new Color(image1.getRGB(i, j));
				color2 = new Color(image2.getRGB(i, j));
				r1 = color1.getRed();
				r2 = color2.getRed();
				g1 = color1.getGreen();
				g2 = color2.getGreen();
				b1 = color1.getBlue();
				b2 = color2.getBlue();
				r = Math.min(r1+r2, 255);
				g = Math.min(g1+g2, 255);
				b = Math.min(b1+b2, 255);
				result = new Color(r, g, b);
				alteredImage.setRGB(i, j, result.getRGB());
			}
		}
		return alteredImage;
	}

}
