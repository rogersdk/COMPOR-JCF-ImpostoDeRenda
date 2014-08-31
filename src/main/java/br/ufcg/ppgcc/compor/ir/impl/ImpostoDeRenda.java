package br.ufcg.ppgcc.compor.ir.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.ufcg.ppgcc.compor.ir.Dependente;
import br.ufcg.ppgcc.compor.ir.ExcecaoImpostoDeRenda;
import br.ufcg.ppgcc.compor.ir.FachadaExperimento;
import br.ufcg.ppgcc.compor.ir.FontePagadora;
import br.ufcg.ppgcc.compor.ir.Resultado;
import br.ufcg.ppgcc.compor.ir.Titular;

public class ImpostoDeRenda implements FachadaExperimento {

	private Map<Titular, List<FontePagadora>> historicoTitularFonte = new LinkedHashMap<Titular, List<FontePagadora>>();
	private Map<Titular, List<Dependente>> mapaTitularDependente = new LinkedHashMap<Titular, List<Dependente>>();

	public void criarNovoTitular(Titular titular) {
		if (titular.getNome() == null) {
			throw new ExcecaoImpostoDeRenda("O campo nome é obrigatório");
		}

		if (titular.getCpf() == null) {
			throw new ExcecaoImpostoDeRenda("O campo CPF é obrigatório");
		}

		if (!ValidarCpf.validarCpf(titular.getCpf())) {
			throw new ExcecaoImpostoDeRenda("O campo CPF está inválido");
		}

		historicoTitularFonte.put(titular, new ArrayList<FontePagadora>());
		mapaTitularDependente.put(titular, new ArrayList<Dependente>());
	}

	public List<Titular> listarTitulares() {
		return new ArrayList<Titular>(historicoTitularFonte.keySet());
	}

	public void criarFontePagadora(Titular titular, FontePagadora fonte) {
		if (fonte.getNome() == null) {
			throw new ExcecaoImpostoDeRenda("O campo nome é obrigatório");
		}

		if (fonte.getRendimentoRecebidos() == 0.0) {
			throw new ExcecaoImpostoDeRenda(
					"O campo rendimentos recebidos é obrigatório");
		} else if (fonte.getRendimentoRecebidos() < 0.0) {
			throw new ExcecaoImpostoDeRenda(
					"O campo rendimentos recebidos deve ser maior que zero");
		}

		if (fonte.getCpfCnpj() == null) {
			throw new ExcecaoImpostoDeRenda("O campo CPF/CNPJ é obrigatório");
		} else if (!fonte.getCpfCnpj().matches(
				"[\\d]{2}\\.[\\d]{3}\\.[\\d]{3}\\/[\\d]{4}\\-[\\d]{2}")) {
			throw new ExcecaoImpostoDeRenda("O campo CPF/CNPJ é inválido");
		}

		if (historicoTitularFonte.containsKey(titular)) {
			List<FontePagadora> listaDeFontesDoTitular = historicoTitularFonte
					.get(titular);
			listaDeFontesDoTitular.add(fonte);
			historicoTitularFonte.put(titular, listaDeFontesDoTitular);
		} else {
			throw new ExcecaoImpostoDeRenda("Titular não cadastrado");
		}
	}

	public List<FontePagadora> listarFontes(Titular titular) {
		return historicoTitularFonte.get(titular);
	}

	public void criarDependente(Titular titular, Dependente dependente) {
		if (dependente.getCpf() == null) {
			throw new ExcecaoImpostoDeRenda("O campo CPF é obrigatório");
		}

		if (dependente.getNome() == null) {
			throw new ExcecaoImpostoDeRenda("O campo nome é obrigatório");
		}

		if (dependente.getTipo() == 0) {
			throw new ExcecaoImpostoDeRenda("O campo tipo é obrigatório");
		} else if (dependente.getTipo() < 0) {
			throw new ExcecaoImpostoDeRenda("O campo tipo é inválido");
		}

		if (!ValidarCpf.validarCpf(dependente.getCpf())) {
			throw new ExcecaoImpostoDeRenda("O campo CPF é inválido");
		}

		if (mapaTitularDependente.containsKey(titular)) {
			List<Dependente> listaDeDependentesDoTitular = mapaTitularDependente
					.get(titular);
			listaDeDependentesDoTitular.add(dependente);
			mapaTitularDependente.put(titular, listaDeDependentesDoTitular);
		} else {
			throw new ExcecaoImpostoDeRenda("Titular não cadastrado");
		}
	}

	public List<Dependente> listarDependentes(Titular titular) {
		return mapaTitularDependente.get(titular);
	}

	public Resultado declaracaoCompleta(Titular titular) {
		Resultado resultado = new Resultado();
		double somatorioImpostoDevido = 0;
		for (FontePagadora fp : listarFontes(titular)) {
			somatorioImpostoDevido += fp.getRendimentoRecebidos();
		}
		
		if (somatorioImpostoDevido < 19.000) {
			resultado.setImpostoDevido(0);
		}
		return resultado;
	}

}
