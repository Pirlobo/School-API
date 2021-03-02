package com.bezkoder.springjwt.payload.request;

import java.util.List;

public class RegisterRequest {
		private List<Integer> regIdClasses;

		public List<Integer> getRegIdClasses() {
			return regIdClasses;
		}

		public void setRegIdClasses(List<Integer> regIdClasses) {
			this.regIdClasses = regIdClasses;
		}

		public RegisterRequest(List<Integer> regIdClasses) {
			super();
			this.regIdClasses = regIdClasses;
		}

		public RegisterRequest() {
			super();
		}
		
		
	

}
