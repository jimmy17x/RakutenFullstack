package com.rakuten.fullstackrecruitmenttest.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Constants {
	public static final int EMPL_NAME_IDX = 0;
	public static final int EMPL_DEPT_IDX = 1;
	public static final int EMPL_DESG_IDX = 2;
	public static final int EMPL_SALARY_IDX = 3;
	public static final int EMPL_JOINING_DATE_IDX = 4;
	public static final int TOTAL_COL = 5;
	public static final Set<String> DESGINATION_SET = new HashSet<>(
			Arrays.asList("Developer", "Senior Developer", "Manager", "Team Lead", "VP", "CEO"));

}
