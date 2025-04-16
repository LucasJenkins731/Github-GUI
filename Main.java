import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.Graphics;


public class Main {
    JFrame frame;
    JPanel panel;


    public Main() throws IOException {
        BufferedImage image = ImageIO.read(new File("QUxMS Logo.png"));
		
		frame = new JFrame();
		frame.add(new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponents(g);
				g.drawImage(image, 150, 0, null);
			}
		});
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(900, 600);
		frame.setLocationRelativeTo(null);
    }


    public static void main(String[] args) throws IOException {
        new Main();
    }
}