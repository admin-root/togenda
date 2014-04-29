///* Danny Gonzalez
// * February 4, 2014
// * Description: Using a one of six color palates given, the user will change either a single, or row
// * 				and column, of a grid into a different color. The user will first click on any of the
// * 				color palates to select which color they want to use, and then select which button 
// * 				within the grid that they want the change to occur/originate at. There will be a check
// * 				box to determine if only a single cell or row-and-column will change.
// */
//package test;
//import android.os.Bundle;
//import android.app.Activity;
//import android.graphics.drawable.ColorDrawable;
//import android.view.Menu;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.GridLayout;
//import android.widget.GridLayout.LayoutParams;
//import android.widget.LinearLayout;
//
//public class MainActivity extends Activity implements OnClickListener{
//	//instance variables
//	private Button[][] grid;
//	ColorDrawable newColor,defaultColor;
//	Button addButton;
//	
//	//constant variables
//	private static final int COLUMN_AMOUNT = 9; //x-axis, horizontal section
//	private static final int ROW_AMOUNT = 9; //y-axis, vertical section
//	private static final int SIZE = 72;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//		
//		GridLayout buttonLayout = (GridLayout)findViewById(R.string.buttonHolder);
//		buttonLayout.setColumnCount(COLUMN_AMOUNT);
//		buttonLayout.setRowCount(ROW_AMOUNT);
//		grid = new Button[ROW_AMOUNT][COLUMN_AMOUNT];
//		//design the layout of the button
//		LayoutParams buttonParams;
//		for(int row=0; row<ROW_AMOUNT; row++)
//		{
//			for(int column=0; column<COLUMN_AMOUNT; column++)
//			{
//				//creating the buttons and assigning it an onClick listener
//				addButton = new Button(getApplicationContext());
//				addButton.setBackgroundColor(getResources().getColor(R.color.grey));
//				addButton.setOnClickListener(this);
//				//setting the parameters for the buttons
//				buttonParams = new LayoutParams();
//				buttonParams.setMargins(2,2,2,2);
//				buttonParams.width = SIZE;
//				buttonParams.height = SIZE;
//				//making buttonParams addButton's main parameters
//				addButton.setLayoutParams(buttonParams);
//				buttonLayout.addView(addButton, buttonParams);
//				grid[row][column] = addButton; //adding a button to each spot in the grid
//			}
//		}
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//	
//	//setting the palate color
//	public void getColor(View v)
//	{
//		Button colorSelect = (Button)(v);
////		if(colorSelect.equals(null))
////		{
////			addButton.setBackgroundColor(getResources().getColor(R.color.red));
////			defaultColor = (ColorDrawable)colorSelect.findViewById(R.id.redButton).getBackground();
////		}
////		else
////		{
//			newColor = (ColorDrawable)colorSelect.getBackground();
////		}
//	}
//	
//	public void onClick(View v) {
//		//calling CheckBox singleCellCheckbox
//		CheckBox checkBox = (CheckBox)findViewById(R.id.singleCellCheckbox);
//		Button picked = (Button)(v);
//		picked.setBackgroundColor(getResources().getColor(R.color.red));
//		//place a try/catch to prevent error if user clicks on the grid first than a color palate
////		try
////		{
//			//only fill-in one button
//			if(checkBox.isChecked() == true)
//			{
//				for(int row=0; row<ROW_AMOUNT; row++)
//				{
//					for(int column=0; column<COLUMN_AMOUNT; column++)
//					{
//						if(picked.equals(grid[row][column]))
//						{
//							grid[row][column] = picked;
//						}
//					}
//				}
//				picked.setBackgroundColor(newColor.getColor()); //problem here
//			}
//			//fill button's row and column
//			else
//			{
//				int horizontal = 0; //column filled
//				int vertical = 0; //row filled
//				//gets the row and column locations
//				for(int row=0; row<ROW_AMOUNT; row++)
//				{
//					for(int column=0; column<COLUMN_AMOUNT; column++)
//					{
//						if(picked.equals(grid[row][column]))
//						{
//							horizontal = column;
//							vertical = row;
//						}
//					}
//				}
//				//fill in rows
//				for(int column=0; column<COLUMN_AMOUNT; column++)
//				{
//					picked = grid[vertical][column];
//					picked.setBackgroundColor(newColor.getColor());
//				}
//				//fill in columns
//				for(int row=0; row<ROW_AMOUNT; row++)
//				{
//					picked = grid[row][horizontal];
//					picked.setBackgroundColor(newColor.getColor());
//				}
//			}
//
////		}
////		catch(NullPointerException ex)
////		{
////			defaultColor = (ColorDrawable)picked.findViewById(R.id.greenButton).getBackground();
////		}
//	}
//}
