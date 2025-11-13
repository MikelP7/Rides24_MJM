package iterator;

import java.util.List;
import java.util.Vector;

public class DepartingCitiesIterator implements ExtendedIterator<String>{

	private List<String> DepartingCities = new Vector<String>();
    private int index;
	
    public DepartingCitiesIterator(List<String> cities) {
    	this.DepartingCities = cities;
    	this.index = 0;
    }
    
	@Override
	public boolean hasNext() {
		return index < DepartingCities.size();
	}

	@Override
	public String next() {
		String city = DepartingCities.get(index);
		index++;
		return city;
	}

	@Override
	public String previous() {
		String city = DepartingCities.get(index);
		index--;
		return city;
	}

	@Override
	public boolean hasPrevious() {
		return index >= 0;
	}

	@Override
	public void goFirst() {
		index = 0;
		
	}

	@Override
	public void goLast() {
		index = DepartingCities.size()-1;
	}

}
