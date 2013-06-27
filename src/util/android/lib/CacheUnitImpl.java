package util.android.lib;

public abstract class CacheUnitImpl implements CacheUnitInterface {
	
	@Override
	public String getHashName(String key) {
		int hashnum = new HashCodeBuilder().append(key).toHashCode();
		return Integer.toHexString(hashnum);
	}
}
