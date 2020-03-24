/* ###
 * IP: GHIDRA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package first;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import org.json.*;
import docking.ActionContext;
import docking.ComponentProvider;
import docking.action.DockingAction;
import docking.action.ToolBarData;
import first.PopulateFunctions.modelFunction;
import ghidra.app.ExamplesPluginPackage;
import ghidra.app.plugin.PluginCategoryNames;
import ghidra.app.plugin.ProgramPlugin;
import ghidra.framework.plugintool.*;
import ghidra.framework.plugintool.util.PluginStatus;
import ghidra.program.model.address.AddressSetView;
import ghidra.util.HelpLocation;
import ghidra.util.Msg;
import resources.Icons;

/**
 * TODO: Provide class-level documentation that describes what this plugin does.
 */
//@formatter:off
@PluginInfo(
	status = PluginStatus.STABLE,
	packageName = ExamplesPluginPackage.NAME,
	category = PluginCategoryNames.EXAMPLES,
	shortDescription = "First is a plugin to check/ add function to database First .",
	description = "Plugin long description goes here."
)
//@formatter:on
public class FirstPlugin extends ProgramPlugin  {

	MyProvider provider;
	
	  static ArrayList<modelFunction > aList =  
              new ArrayList<modelFunction >(); 
              private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

	/**
	 * Plugin constructor.
	 * 
	 * @param tool The plugin tool that this plugin is added to.
	 */
	public FirstPlugin(PluginTool tool) {
		super(tool, true, true);

		// TODO: Customize provider (or remove if a provider is not desired)
		String pluginName = getName();
		provider = new MyProvider(this, pluginName);
		// TODO: Customize help (or remove if help is not desired)
		String topicName = this.getClass().getPackage().getName();
		String anchorName = "HelpAnchor";
		provider.setHelpLocation(new HelpLocation(topicName, anchorName));
		
		
	}

	@Override
	public void init() {
		super.init();

		// TODO: Acquire services if necessary
	}

	// TODO: If provider is desired, it is recommended to move it to its own file
	private static class MyProvider extends ComponentProvider  implements ActionListener{

		private JPanel panel;
		private DockingAction action;
		private JButton loadFunctions;
	    private JTable table;
		private DefaultTableModel tableModel;
	    private JPopupMenu popupMenu;
	    private JMenuItem menuItemAdd;
	    private JMenuItem menuItemCheck;
	    private JMenuItem menuItemShowDetails;
	    private JMenuItem menuItemColor;
	    private JMenuItem menuItemRemove;
	    private JMenuItem menuItemRemoveAll;
	    
		public MyProvider(Plugin plugin, String owner) {
			super(plugin.getTool(), owner, owner);
			buildPanel();
			createActions();
		
		}	
		// Customize GUI
		private void buildPanel() {
			 panel = new JPanel(new BorderLayout());
			 popupMenu = new JPopupMenu();
			 menuItemAdd = new JMenuItem("Add to First");
			 menuItemCheck = new JMenuItem("Check From First");
			 menuItemShowDetails = new JMenuItem("Show Details");
			 menuItemColor = new JMenuItem("Color");
			 menuItemRemove = new JMenuItem("Remove");
			 menuItemRemoveAll = new JMenuItem("Remove All");

			 
	        menuItemAdd.addActionListener(this);
	        menuItemCheck.addActionListener(this);
	        menuItemShowDetails.addActionListener(this);
	        menuItemColor.addActionListener(this);
	        menuItemRemove.addActionListener(this);
	        menuItemRemoveAll.addActionListener(this);
	        
			popupMenu.add(menuItemAdd);
			popupMenu.add(menuItemCheck);
			popupMenu.add(menuItemShowDetails);
			popupMenu.add(menuItemColor);
			popupMenu.add(menuItemRemove);
			popupMenu.add(menuItemRemoveAll);
			
			
			panel.setBorder(BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(), "Functions List", TitledBorder.CENTER, TitledBorder.TOP));
			loadFunctions = new JButton("Load Functions");
			loadFunctions.setBounds(50,100,95,30);  
			loadFunctions.addActionListener(this);	
			populateTable();
			
			
			JTableHeader headerlabel = table.getTableHeader();
			headerlabel.setBackground(Color.BLACK);
			headerlabel.setForeground(Color.BLUE);	
			table.setComponentPopupMenu(popupMenu);
			table.addMouseListener(new TableMouseListener(table));
			table.setModel(tableModel);
			tableModel.fireTableDataChanged();
			panel.add(new JScrollPane(table));			
		
			panel.add(loadFunctions, BorderLayout.NORTH);
			setVisible(true);
		}		
		
	public void  populateTable()
	{
		String[] header = { "Id", "Name", "Opcode","Prototype","Comment" };
		
		Object[][] fileList = new Object[aList.size()][5];

		for (int i = 0; i < aList.size(); i++) {
			
		    fileList [i][0] = aList.get(i).idfunction;
		    fileList [i][1] = aList.get(i).namefunction;
		    fileList [i][2] = aList.get(i).bodyfunction;
		    fileList [i][3] = aList.get(i).prototypefunction;
		    fileList [i][4] = aList.get(i).comment;	 
		    }		
		 
		tableModel = new DefaultTableModel(fileList, header); 
		//table.setModel(tableModel);
		table = new JTable(tableModel);
	}
		// TODO: Customize actions
		private void createActions() {
			action = new DockingAction("My Action", getName()) {
				@Override
				public void actionPerformed(ActionContext context) {
					Msg.showInfo(getClass(), panel, "Custom Action", "Hello!");      	
				}
			};
			action.setToolBarData(new ToolBarData(Icons.ADD_ICON, null));
			action.setEnabled(true);
			action.markHelpUnnecessary();
			dockingTool.addLocalAction(this, action);
		}

		@Override
		public JComponent getComponent() {
			return panel;
		}
		
		
	    public void actionPerformed(ActionEvent event) {
	    	
	        Object  menu =  event.getSource();
	        if (menu ==loadFunctions)
	        {
	        	
					PopulateFunctions pf =new PopulateFunctions();	
		        	try {
						pf.run();		
						
						 aList= pf.getFuntions();
						

						// tableModel.fireTableDataChanged();
						// populateTable();
						// table.repaint();
				    	 //table.repaint();
				    	// provider = new MyProvider(this, pluginName);
						buildPanel();
				    	 
					} catch (Exception ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}
					
				}
	    
	    else  if (menu == menuItemAdd) {
	           try {
				addNewRow();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        	//Msg.showInfo(getClass(), panel, "Custom Action", "menuItemAdd!");
	        } else if (menu == menuItemCheck) {
	        	try {
					checkFunctionFirst();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}        

	        } else if (menu == menuItemShowDetails) {
	        	Msg.showInfo(getClass(), panel, "Custom Action", "menuItemShowDetails!");
	        } else if (menu == menuItemColor) {
	        	Msg.showInfo(getClass(), panel, "Custom Action", "menuItemColor!");
	        }
	        else if (menu == menuItemRemove) {
	        	removeCurrentRow();
	        }
	        else if (menu == menuItemRemoveAll) {
	        	removeAllRows(); 
	        }        
	        
	    }

	    private void refresh() {
	    	 
	  	          tableModel.fireTableDataChanged();  	        
	  	    
	    }
	    
	    private void checkFunctionFirst() throws Exception {
	    	int selectedRow = table.getSelectedRow();
	    	String opCodeFunction =tableModel.getValueAt(selectedRow, 1).toString();	
	    	AddressSetView adressSetView = (AddressSetView) tableModel.getValueAt(selectedRow,2);	
	    	//Calcul the md5 of an opcode   
	    	MessageDigest messageDigest = MessageDigest.getInstance("MD5");
	    	messageDigest.update(opCodeFunction.getBytes());
	    	byte[] digiest = messageDigest.digest();
	    	char[] hexChars = new char[digiest.length * 2];
	    	for (int j = 0; j < digiest.length; j++) {
	    		int v = digiest[j] & 0xFF;
	    		hexChars[j * 2] = HEX_ARRAY[v >>> 4];
	    		hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
	    		
	    	}
	    	String md5Function = String.valueOf(hexChars);
	    	System.out.println(md5Function);
	    	
	    	//calcul crc32 d'un opCode
	    	byte[] bytes = opCodeFunction.getBytes();
	        Checksum checksum = new CRC32(); // java.util.zip.CRC32
	        checksum.update(bytes, 0, bytes.length);
	        //System.out.println(checksum.getValue());
	      
	       String crc32 = String.valueOf(checksum.getValue()); 
	       httpService myService = new httpService("https://louishusson.com/api/", "BFBFC6FC-4C84-4299-B2F6-7335C479810D");
	       JSONObject result = myService.checkInFirst(md5Function,crc32);
			if(result.getBoolean("failed")==false) {  
				//System.out.println("Request Success");
				//System.out.println("checkin = " + result.get("checkin"));
				Msg.showInfo(getClass(), panel, "Check First", " Request : Success \n Checking : " +result.get("checkin"));
				//SetColorCommand sc = new SetColorCommand(new Color(255, 200, 200), current, adressSetView);
				///sc.applyTo(obj)
				//SetColorCommand..setBackgroundColor(adressSetView , new Color(255, 200, 200));	
				}
			else
				//System.out.println("Request failed");
			Msg.showError(getClass(), panel,  "Check First", "Request : Failed");
	    }
	    private void addNewRow() throws Exception {
	       // tableModel.addRow(new String[0]);	    	
	    }
	     
	    private void removeCurrentRow() {
	        int selectedRow = table.getSelectedRow();
	        tableModel.removeRow(selectedRow);
	    }
	     
	    private void removeAllRows() {
	        int rowCount = tableModel.getRowCount();
	        for (int i = 0; i < rowCount; i++) {
	            tableModel.removeRow(0);
	        }
	    }
	}
}
