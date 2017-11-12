package opencvFAST;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class FAST {
	
	static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

	public static void main(String[] args) {
		String filePath = "src/test.png";
		Mat Image = Imgcodecs.imread(filePath);
		if(Image.dataAddr()==0){
			System.out.println("Erreur d'ouverture de fichier " + filePath);
		}
		else 
		{
			Mat ImageGray = new Mat(Image.height(),Image.width(),CvType.CV_8UC1);
			
			Imgproc.cvtColor(Image, ImageGray, Imgproc.COLOR_BGR2GRAY);			
			int totalBytes = (int)(ImageGray.total()*ImageGray.elemSize());
			double buffer[] = new double[totalBytes];			
			
			for (int i = 0; i < ImageGray.height(); i++) {
				for (int j = 0; j < ImageGray.width(); j++) {
					buffer[i*ImageGray.cols()+j] = ImageGray.get(i, j)[0];
				}
			}
			ImageGray.put(0, 0, buffer);
			
			ImageViewer v = new ImageViewer();
			v.show(ImageGray);
			
			List<Px> corners = new ArrayList<>();
			
			int r = 3; // rayon 
			
			for (int i = r; i < ImageGray.height()-r; i++) {
				
				for (int j = r; j  < ImageGray.width()-r; j++) {
					
					int pc = 0; // nbr pts foncés
					int pf = 0; // nbr pts clairs
					
					List<Px> circle = new ArrayList<>();
					
					int length = r*4+5; 
					char[] t = new char[length];
					
					t[0] = buffer[i*ImageGray.cols()+j] > 128 ? 'c' : 'f'; // c -> couleur clair, f -> couleur foncé
					
					
					
					
					
					
					
					int i2 = i - r; // start from circle's top left
					int j2 = j - r/2;
										
					int k = 1;
					int lim = k+r;
					for (; k < lim; k++) {
						
						 t[k] = t[0] - buffer[i2*ImageGray.cols()+j2] > 20 ? 'f' : 'c';
						 
						 if(t[k] == 'c')
							 pc++;
						 else
							 pf++;
						 
						 circle.add(new Px(i2, j2));
						
						 j2++;
					}
					i2++;
					t[k] = t[0] - buffer[i2*ImageGray.cols()+j2] > 20 ? 'f' : 'c';
					circle.add(new Px(i2, j2));
					if(t[k] == 'c')
						 pc++;
					 else
						 pf++;
					i2++;
					j2++;
					k++;
					lim = k+r;
					for (; k < lim; k++) {
						
						 t[k] = t[0] - buffer[i2*ImageGray.cols()+j2] > 20 ? 'f' : 'c';
						 
						 if(t[k] == 'c')
							 pc++;
						 else
							 pf++;
						 circle.add(new Px(i2, j2));
						
						 i2++;
					}
					
					j2--;
					t[k] = t[0] - buffer[i2*ImageGray.cols()+j2] > 20 ? 'f' : 'c';
					circle.add(new Px(i2, j2));
					if(t[k] == 'c')
						 pc++;
					 else
						 pf++;
					i2++;
					j2--;
					k++;
					lim = k+r;
					for (; k < lim; k++) {
						
						 t[k] = t[0] - buffer[i2*ImageGray.cols()+j2] > 20 ? 'f' : 'c';
						 circle.add(new Px(i2, j2));
						 
						 if(t[k] == 'c')
							 pc++;
						 else
							 pf++;
						
						 j2--;
					}
					
					i2--;
					t[k] = t[0] - buffer[i2*ImageGray.cols()+j2] > 20 ? 'f' : 'c';
					circle.add(new Px(i2, j2));
					if(t[k] == 'c')
						 pc++;
					 else
						 pf++;
					i2--;
					j2--;
					k++;
					lim = k+r;
					for (; k < lim; k++) {
						
						 t[k] = t[0] - buffer[i2*ImageGray.cols()+j2] > 20 ? 'f' : 'c';
						 circle.add(new Px(i2, j2));
						 
						 if(t[k] == 'c')
							 pc++;
						 else
							 pf++;
						
						 i2--;
					}
					j2++;
					t[k] = t[0] - buffer[i2*ImageGray.cols()+j2] > 20 ? 'f' : 'c';
					circle.add(new Px(i2, j2));
					if(t[k] == 'c')
						 pc++;
					 else
						 pf++;
					k++;
					
					
					
					
					
					
										
					int nbrChangements = 0;
					
					for (int l = 2; l < length; l++) {
						if(t[l] != t[l-1])
							nbrChangements++;
					}
					
					if(nbrChangements <= 2)
					{
						if((pc >= ((length-1)*0.75)-1 && t[0] == 'f') || (pf >= ((length-1)*0.75)-1 && t[0] == 'c'))
						{
							corners.add(new Px(i, j));
							double[] cornerColor =  t[0] == 'f' ? new double[] {0, 0, 255} : new double[] {255, 0, 0};
							Image.put(i, j, cornerColor); // change corner's color to red or blue
							for(Px p : circle)
							{
								Image.put(p.i, p.j, cornerColor);
							}
						}
						
					}
					
				}
				
				
			}
			
			ImageViewer v2 = new ImageViewer();
			v2.show(Image);
			
			
		}
	}
	
	
}
