package airline_reservation_system;


public class Reservation
{
	String flightNum;
	String flightInfo;
	boolean firstClass;
	String couponCode;
	int price;
	public Reservation(String flightNum, String info, String couponCode, int price)
	{
		this.flightNum = flightNum;
		this.flightInfo = info;
		this.firstClass = false;
		this.couponCode = couponCode;
		this.price = price;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
	public boolean isFirstClass()
	{
		return firstClass;
	}

	public void setFirstClass()
	{
		this.firstClass = true;
	}

	public String getFlightNum()
	{
		return flightNum;
	}
	
	public String getFlightInfo()
	{
		return flightInfo;
	}

	public void setFlightInfo(String flightInfo)
	{
		this.flightInfo = flightInfo;
	}

	public void print()
	{
		System.out.println(flightInfo);
	}

	@Override
	public String toString() {
		return "Reservation{" +
				"flightNum='" + flightNum + '\'' +
				", flightInfo='" + flightInfo + '\'' +
				", firstClass=" + firstClass +
				", couponCode='" + couponCode + '\'' +
				", price=" + price +
				'}';
	}
}
