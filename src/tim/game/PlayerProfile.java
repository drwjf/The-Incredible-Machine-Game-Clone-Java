/** 
 * PlayerProfile class
 * CIS 3750 Group 4
 * Contains various methods for use with manipulating player profile data
 * @author Kiril Goguev
 */
package tim.game;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;

public class PlayerProfile
{
	private String playerName;
	private int unlockedLevels;
	private int currentScore;
	private int currency;
	private PlayerSetting playerSet;
	
	/** 
	 * Checks to see if there are profiles on the system 
	 * by checking to see if the directory is empty
	 * @return Whether or not the profile directory exists.
	 */
	public Boolean Check()
	{
		File ProfileDirectory = new File ("profiles/");

		if (!ProfileDirectory.exists()) {
			try{
				ProfileDirectory.mkdir();
		        return false;
		     } catch(SecurityException se){
		        //handle it
		     }    
		}
		
		
		if(ProfileDirectory.listFiles().length==0)
		{
			    
			return false;
		}
		else
		{
			return true;
		}
	}
	
	
	/**
	 * Creates a player profile 
	 * Checks if there is a profile by that name already in the system
	 */
	public void Create()
	{
		String Name;
		int Unlocked_levels=1;
		int Score=0;
		PlayerSetting ps = new PlayerSetting("normal", "on", "on", 10);
		File ProfileFile;
		FileWriter pf;
		
		JFrame Profile = new JFrame("Create Profile");
		
		//Asks the user for their player Name & creates the profile in ./tim/profiles if the user clicks ok and the input field is not null and not empty
		try
		{
			Name = JOptionPane.showInputDialog(Profile,"What is your name?","Enter a name for your profile:",JOptionPane.QUESTION_MESSAGE);
			
			if ((Name!=null)&&(Name.length()!=0))
			{
				/*check the profiles directory for a profile with the same name*/
				ProfileFile = new File("profiles/"+Name);
				if (ProfileFile.exists()==true)
				{
					JOptionPane.showMessageDialog(null,"ERROR! profile already exists with that name!");
					Create();
				}
				else
				{
					
					try
					{
						//Creates the profile with the Name that the user typed in and the default values for the file
						pf = new FileWriter(ProfileFile);
						pf.write(Name+"\n");
						pf.write(Unlocked_levels+"\n");
						pf.write(Score+"\n");
						pf.write("0\n");    //currency
						pf.write(ps.toString());
						pf.close();
						
						//loads the values for the current profile
						playerName=Name;
						unlockedLevels=Unlocked_levels;
						currentScore=Score;
						currency = 0;
						playerSet = ps;
						JOptionPane.showMessageDialog(null,"SUCCESS! new profile "+playerName +" created!");
					}
					catch(IOException E)
					{
						JOptionPane.showMessageDialog(null,"FAILURE! new profile "+playerName+ " could not be created!"+E.getMessage());
					}
					
				}
			}
			else if (Name.length()==0)
			{
				JOptionPane.showMessageDialog(null,"ERROR! Name field can not be empty!");
				Create();
			}
		}
		catch(Exception CancelHit)
		{
			JOptionPane.showMessageDialog(null,"Profile not created!");
		}
	}
	
	/** 
	 * If the directory was not empty then the user can load a file from the custom dialog or create a new one
	 * this code is ugly but hey, it works...
	 */		
	public void Load()
	{
		if (this.Check()==true)
		{
			int i;
			final JFrame profileLoader= new JFrame("Load profile");
			final JDialog profileDialog = new JDialog (profileLoader, "Load profile", true);
            profileDialog.setLocationRelativeTo(null);
			final File ProfileDirectory = new File ("profiles/");
			final int numProfiles =  ProfileDirectory.listFiles().length;
			
			StringBuffer[] buttonName = new StringBuffer[numProfiles];
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

			JButton[] profiles = new JButton[numProfiles];
			
			//Create and Cancel are two essential buttons with diffrent ActionListeners
			JButton Create = new JButton("Create New");
			Create.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) { profileLoader.dispose(); Create();}});

			//This is the code responsible for creating each button dynamically and assigning a specific players profile to each 
			//button, each players Name is read in as a string, the number of levels unlocked as int and the players current score as int. 
			for (int k=0;k<numProfiles;k++)
			{
				buttonName[k]=new StringBuffer(ProfileDirectory.listFiles()[k].toString());
			}
			
			
			for( i=0;i<numProfiles;i++)
			{
				final int j=i;
				profiles[i] = new JButton(buttonName[i].substring(9));
				panel.add(Box.createRigidArea(new Dimension(0, 5)));
				profiles[i].addActionListener(new ActionListener() {					  
					public void actionPerformed(ActionEvent event) {

						try
						{
							//players specific file is opened via file assined to Jbutton
							File f = new File(ProfileDirectory.listFiles()[j].toString());
							Scanner in =new Scanner(new FileInputStream(f));
							//reading in from a file is done here
							playerName=in.next();
							unlockedLevels=in.nextInt();
							currentScore=in.nextInt();
							currency = in.nextInt();
							playerSet = new PlayerSetting(in.next(), in.next(), in.next(), in.nextInt());
							in.close();
							JOptionPane.showMessageDialog(null,"SUCCESS!  profile "+playerName+" loaded!");
							profileLoader.dispose();
						}
						catch(Exception IOfile)
						{
						JOptionPane.showMessageDialog(null,"FAILURE!  profile "+ProfileDirectory.listFiles()[j].toString() +" cant be loaded!");
						profileLoader.dispose();
						}
						}});
				panel.add(profiles[i]);
			}
			panel.add(Box.createRigidArea(new Dimension(0, 5)));
			panel.add(Create);
			profileDialog.add(panel);
			profileDialog.setSize(200,300);
			profileDialog.setVisible(true);
			
		}
	}
	
	/**
	 * Updates the profile that this object is associated with. 
	 * @param updateLevel The level that this user has progressed to. 
	 * Please, do not make it < 0.
	 * @param updateScore The score that this user has managed to acquire.
	 * Again, not less than 0. It will not make things explode, but it will
	 * disappoint the user.
	 */
	public void update(int updateLevel,int updateScore)
	{
		//Recreate the profile with the name loaded from a players file(assumes players profile is loaded)
		File ProfileFile = new File("profiles/"+playerName);
		
		try
		{
			FileWriter pf = new FileWriter(ProfileFile);
			pf.write(playerName+"\n");
			pf.write(updateLevel+"\n");
			pf.write(updateScore+"\n");
			pf.write(currency+"\n");
			pf.write(playerSet.toString());
			pf.close();
		}
		catch(Exception E)
		{
			JOptionPane.showMessageDialog(null,"FAILURE! profile "+playerName+" not updated!");
		}
		
		
	}

	/**
	 * Updates the profile that this object is associated with. 
	 * updates the profile without any arguments (assuming that u used the public setMethods())
	 */
	public void update()
	{
		//Recreate the profile with the name loaded from a players file(assumes players profile is loaded)
		File ProfileFile = new File("profiles/"+playerName);
		
		try
		{
			FileWriter pf = new FileWriter(ProfileFile);
			pf.write(playerName+"\n");
			pf.write(unlockedLevels+"\n");
			pf.write(currentScore+"\n");
			pf.write(currency+"\n");
			pf.write(playerSet.toString());
			pf.close();
		}
		catch(Exception E)
		{
			JOptionPane.showMessageDialog(null,"FAILURE! profile "+playerName+" not updated!");
		}
		
		
	}	

	
	/**
	 * Retrieves the name of the profile.
	 * @return The user's name, that they have made for the profile.
	 */
	public String getName()
	{
		return playerName;
	}
	
	/**
	 * Retrieves the score associated with this profile.
	 * @return The user's score that they have accumulated.
	 */
	public int getScore()
	{
		return currentScore;
	}
	
	/**
	 * Retrieves the number of levels unlocked.
	 * @return The level that was last unlocked.
	 */
	public int getLevels() {
		return unlockedLevels;
	}
	
	/**
	 * Retrieves the money that the user has at the moment.
	 * @return The amount of money stored away by the user.
	 */
	public int getCurrency() {
		return currency;
	}

	/**
	 * Retrieves the player's settings that has been saved.
	 *
	 * @return the setting's for the player
	 */
	public PlayerSetting getPlayerSetting() {
		return playerSet;
	}
	
	/**
	 * Sets the score of the user to the parameter.
	 * @param score The score to set the user's total to.
	 */
	public void setScore(int score) {
		currentScore = score;
	}
	
	/**
	 * Unlocks all levels up to and including this level number.
	 * @param level The levels that are to be unlocked, every puzzle
	 * below this number and including.
	 */
	public void setLevels(int level) {
		unlockedLevels = level;
	}
    
	
	/**
	 * Sets the money that the user has to what was passed in.
	 * @param monies The amount to increase/decrease the stored currency to.
	 */
	public void setCurrency(int monies) {
		currency = monies;
	}
}

