package myClient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
//import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import com.sun.java.swing.plaf.windows.*;
 
/**
 * This program shows a "Login" window based on Swing JFrame.
 * When you input the correct userID and Password, you can obtain a confirmation,
 * or else you will be alerted by a JAVA standard message window.
 * <p>
 * The Swing JFrame used in the same time the GridLayout for the Container and the FlowLayout for the JPanel.
 * @author han
 *
 */
public class frame {
	
	public static String str = null;
	
	public static final JTextField urlText=new JTextField(25);
	public static final JTextArea srcArea=new JTextArea(35,35);
	

    JFrame frame=new JFrame();
    Container c=frame.getContentPane();
 

	public frame() {
		
		try {

			UIManager.setLookAndFeel(new WindowsLookAndFeel());

		} 
		catch (UnsupportedLookAndFeelException e) 
		{

			e.printStackTrace();

		} 
		
		
		
		
		frame.setTitle("客户端");
		frame.setSize(800, 500);
		frame.setLocationRelativeTo(null);
		
		c.setLayout(new BorderLayout());					//设置总体布局
		JMenuBar menuBar = new JMenuBar();					//设置菜单
		JMenu menu = new JMenu("File");
		JMenuItem menuItem = new JMenuItem("Save");
		
		menuBar.add(menu);
		menu.add(menuItem);
		frame.setJMenuBar(menuBar);
		JPanel northPanel=new JPanel();
		JPanel centerPanel=new JPanel();
		JPanel buttenPanel=new JPanel();					//存放按钮的面板
		northPanel.setBackground(Color.white);				//设置背景颜色
		centerPanel.setBackground(Color.cyan);
		buttenPanel.setBackground(Color.white);
		northPanel.setPreferredSize(new Dimension(0,35));	//设置北区面板的高度为38，宽度随窗口变化
        northPanel.setLayout(new BorderLayout());			//北区使用边框布局
        centerPanel.setLayout(new BorderLayout());			//中心区使用边框布局		
		
        northPanel.add(buttenPanel,BorderLayout.EAST);		//将按钮面板添加到northPanel面板的东区        
		
        //设置URL标签
		JLabel urlLable=new JLabel("URL：");
		urlLable.setBorder(new EmptyBorder(0,5,0,0));
		urlLable.setPreferredSize(new Dimension(40,0));
		northPanel.add(urlLable,BorderLayout.WEST);
		
		
		//网址文本框
       
        urlText.setBorder(new CompoundBorder(new EmptyBorder(5,0,5,0), new LineBorder(Color.gray)));//设置文本框的位置和显示文本框的边框
        northPanel.add(urlText,BorderLayout.CENTER);
		
		
		//设置文本域
        JScrollPane scrollPane=new JScrollPane(srcArea);//给文本域添加滚动条
        scrollPane.setBorder(new CompoundBorder(new EmptyBorder(0,5,15,5), new LineBorder(Color.gray)));//设置文本框的位置和显示文本域的边框
        centerPanel.add(scrollPane);

		srcArea.setEditable(false);
		
		//设置按钮
		
		final JButton button1 = new JButton("Go");
		final JButton button2 = new JButton("Reset");
		
		northPanel.add(urlText);
		
		buttenPanel.add(button1);
		buttenPanel.add(button2);
		
		centerPanel.add(srcArea);
		
        c.add(northPanel, BorderLayout.NORTH);
		c.add(centerPanel, BorderLayout.CENTER);
		
		button1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				str=urlText.getText();
				try {
					srcArea.setText("");
					cilent.runSocket();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}			
		});
		//清空
		button2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				urlText.setText("");
				srcArea.setText("");	
				
			}		
		});
		
		//下载
		menuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				
				javax.swing.filechooser.FileFilter filter = new javax.swing.filechooser.FileFilter() {     
					public boolean accept(File f) 
					{      
						return f.isDirectory() || (f.isFile() && (f.getName().endsWith(".htm")|| 
							   f.getName().endsWith(".HTM")|| f.getName().endsWith(".html")    || 
							   f.getName().endsWith(".HTML")    )); // 新建一个文件类型过滤器    
					}     
					public String getDescription() 
					{     
			 			return "保存为HTML文件格式";     
					}    
				}; 
				
				JFileChooser jfCsr = new JFileChooser();			//创建文件资源管理器
				//jfCsr.setFileSelectionMode(JFileChooser.SAVE_DIALOG | JFileChooser.DIRECTORIES_ONLY);
				jfCsr.setFileFilter(filter);
				int i = jfCsr.showSaveDialog(c);
				String fname = null;
				if(i == JFileChooser.APPROVE_OPTION)		//对象可以使用
				{
					File f = jfCsr.getSelectedFile();
					fname = jfCsr.getName(f);
					if(fname != null && fname.trim().length()>0)	//为未命名文件添加后缀名 
					{      
						if(fname.endsWith(".htm") || fname.endsWith(".HTM") || fname.endsWith(".html") || fname.endsWith(".HTML"));    
						else {       
							f = new File(f.getAbsolutePath().concat(".html"));      
						}  
					}
					if(f.isFile())
						fname = f.getName();				//获取文件名

					if(f.exists())							//判断文件是否存在
					{
						i = JOptionPane.showConfirmDialog(c,"该文件已存在，确定要覆盖吗？");
						if(i == JOptionPane.YES_OPTION);
						else
							return;
					}
					try{
						f.createNewFile();					//建立文件到本地目录
						FileWriter fw = new FileWriter(f);	
						fw.write(srcArea.getText());		//将资源栏中的数据写入到创建的文件
						JOptionPane.showMessageDialog(c, "保存成功");
						fw.close();
					}
					catch(Exception ex)
					{
						JOptionPane.showMessageDialog(c,"出错：" + ex.getMessage());
					}

				}
			}
		});
		

		//menuItem.setFocusable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );
	}


	public static void main(String[] args) {
		new frame();
	}
}
