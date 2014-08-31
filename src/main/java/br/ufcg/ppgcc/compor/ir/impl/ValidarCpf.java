package br.ufcg.ppgcc.compor.ir.impl;


public class ValidarCpf {

	public static boolean validarCpf(String cpf) {
		if (cpf.matches("[\\d]{3}\\.[\\d]{3}\\.[\\d]{3}\\-[\\d]{2}")) {
			return true;
		}
		return false;
	}
}
