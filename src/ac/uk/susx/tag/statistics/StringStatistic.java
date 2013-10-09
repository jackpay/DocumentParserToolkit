package ac.uk.susx.tag.statistics;

public class StringStatistic extends AbstractStatistic<String> {

	public StringStatistic(String stat) {
		super(stat);
	}

	@Override
	public String toString() {
		return getStat();
	}

}
