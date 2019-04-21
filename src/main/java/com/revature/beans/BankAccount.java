package com.revature.beans;

public class BankAccount {

		private int accountnumber;
		private double balance;
		
		public int getAccountnumber() {
			return accountnumber;
		}
		public void setAccountnumber(int accountnumber) {
			this.accountnumber = accountnumber;
		}
		public double getBalance() {
			return balance;
		}
		public void setBalance(double balance) {
			this.balance = balance;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + accountnumber;
			long temp;
			temp = Double.doubleToLongBits(balance);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			BankAccount other = (BankAccount) obj;
			if (accountnumber != other.accountnumber)
				return false;
			if (Double.doubleToLongBits(balance) != Double.doubleToLongBits(other.balance))
				return false;
			return true;
		}
		@Override
		public String toString() {
			return "BankAccount [accountnumber=" + accountnumber + ", balance=" + balance + "]";
		}
		public BankAccount(int accountnumber, double balance) {
			super();
			this.accountnumber = accountnumber;
			this.balance = balance;
		}
		public BankAccount() {
			super();
			// TODO Auto-generated constructor stub
		}
		
		
}
