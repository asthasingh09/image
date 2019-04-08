
package com.mycompany.cloudvision;

import java.awt.Dimension;
import java.io.File;
import javax.swing.JFileChooser;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import org.apache.commons.io.*;
import static jdk.nashorn.internal.objects.NativeArray.map;
import org.json.JSONObject;
import org.json.JSONException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
public class image extends javax.swing.JFrame {

    /**
     * Creates new form image
     */
    public image() {
        initComponents();
    }

     /* This method is called from within the constructor to initialize the form. */
     
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        searchpanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        Open = new javax.swing.JButton();
        selectedfile = new javax.swing.JLabel();

        fileChooser.setDialogTitle("Select File");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Image Search");
        setMaximumSize(new java.awt.Dimension(1000, 1000));
        setPreferredSize(new java.awt.Dimension(500, 500));

        searchpanel.setAutoscrolls(true);
        searchpanel.setMaximumSize(new java.awt.Dimension(1000, 1000));

        jLabel1.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); 
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("view similar images here");
        searchpanel.add(jLabel1);

        Open.setText("Select Image");
        Open.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpenActionPerformed(evt);
            }
        });

        selectedfile.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        selectedfile.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        selectedfile.setText("No File Selected");
        selectedfile.setFocusable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchpanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(Open)
                        .addGap(18, 18, 18)
                        .addComponent(selectedfile, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(275, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectedfile, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Open, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addComponent(searchpanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(456, Short.MAX_VALUE))
        );

        pack();
    }

    private void OpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpenActionPerformed
    
    //    
    int returnVal = fileChooser.showOpenDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        try {
          // What to do with the file, e.g. display it in a TextArea
          
        	String imagePath = ""+file.getAbsolutePath();
		String base64ImageString = encoder(imagePath);
		System.out.println("\n"+base64ImageString);
 
		
        String payload = "{" + 
        		"  \"requests\":[" + 
        		"    {" + 
        		"      \"image\":{" + 
        		"        \"content\":\""+ base64ImageString +"\""+ 
        		"      }," + 
        		"      \"features\":[" + 
        		"        {" + 
        		"          \"type\":\"LABEL_DETECTION\"," + 
        		"          \"maxResults\":1" + 
        		"        }" + 
        		"      ]" + 
        		"    }" + 
        		"  ]" +
                "}";
        
        StringEntity entity = new StringEntity(payload,
                ContentType.APPLICATION_JSON);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("https://vision.googleapis.com/v1/images:annotate?key=AIzaSyCtQ4H8WDhOJMTa1X2JovZfk5d2FtBNAiQ");
        request.setEntity(entity);

        HttpResponse response = httpClient.execute(request);
        System.out.println(response.getStatusLine().getStatusCode());
        
        if((response.getEntity()) != null) {
			HttpEntity entityRes = response.getEntity();
			String responseString = EntityUtils.toString(entityRes, "utf-8");
                        System.out.println(responseString);
                       
            try {
            JSONObject json = new JSONObject(responseString);
            JSONArray result = json.getJSONArray("responses");
            JSONObject result1 = result.getJSONObject(0);
            JSONArray label = result1.getJSONArray("labelAnnotations");
            JSONObject label1 = label.getJSONObject(0);
            String description = label1.getString("description");
            selectedfile.setText("It's an image of "+ description);
            
                        File directory= new File("/Users/asthasingh/Desktop/directory/"+description);
            if(directory.exists()==true){
                
                System.out.println("Exist");
                jLabel1.setVisible(false);
                File[] fList = directory.listFiles();
                int imgno = fList.length;
                String[] names = new String[imgno];
               
                for(int i = 0; i<imgno;i++){
                    names[i] = fList[i].toString();
                    JLabel pic;
                    
                    System.out.println(names[i]);

                    pic = new JLabel(new ImageIcon(names[i]));
                    pic.setPreferredSize(new Dimension(200, 200));
                    searchpanel.add(pic);
                    
             
                }
                revalidate();
                repaint();
                File source = new File(""+file.getAbsolutePath());
                File dest = new File("/Users/asthasingh/Desktop/directory/"+description);
                try {
                    FileUtils.copyFileToDirectory(source, dest);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                System.out.println("Not Exist");
                jLabel1.setText("No Similar Image Found");
                new File("/Users/asthasingh/Desktop/directory/"+description).mkdir();
                File source = new File(""+file.getAbsolutePath());
                File dest = new File("/Users/asthasingh/Desktop/directory/"+description);
                try {
                    FileUtils.copyFileToDirectory(source, dest);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        catch(JSONException e) {
            // Auto-generated catch block
            e.printStackTrace();
        }
			
	}
        
        } catch(IOException ex) {
          System.out.println("problem accessing file"+file.getAbsolutePath());
        }
    } else {
        System.out.println("File access cancelled by user.");
    }
    }//:event_OpenActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new image().setVisible(true);
            }
        });
    }
	public static String encoder(String imagePath) {
		String base64Image = "";
		File file = new File(imagePath);
		try (FileInputStream imageInFile = new FileInputStream(file)) {
			// Reading a Image file from file system
			byte imageData[] = new byte[(int) file.length()];
			imageInFile.read(imageData);
			base64Image = Base64.getEncoder().encodeToString(imageData);
		} catch (FileNotFoundException e) {
			System.out.println("Image not found" + e);
		} catch (IOException ioe) {
			System.out.println("Exception while reading the Image " + ioe);
		}
		return base64Image;
	}
    // Variables declaration - do not modify
    private javax.swing.JButton Open;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel searchpanel;
    private javax.swing.JLabel selectedfile;
    // End of variables declaration
}
