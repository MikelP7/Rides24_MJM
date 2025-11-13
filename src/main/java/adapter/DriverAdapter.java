package adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

import domain.Driver;
import domain.Ride;

public class DriverAdapter extends AbstractTableModel {
	  
	protected Driver driver;
	protected String[] columnNames = new String[] {"From", "To", "Date", "Places", "Price" };

	public DriverAdapter(Driver d) {
		this.driver = d;
	} 

	@Override
	public int getRowCount() {
		return driver.getRides().size();
	}
	
	@Override
	public String getColumnName(int i) {
		return columnNames[i];
	}
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		List<Ride> rides = new ArrayList<>(driver.getRides());
        Ride r = rides.get(rowIndex);
		  
		if (columnIndex == 0) return r.getFrom();
		else if (columnIndex == 1) return r.getTo();
		else if (columnIndex == 2) return r.getDate();
		else if (columnIndex == 3) return r.getnPlaces();
		else return r.getPrice();  
	}
}
