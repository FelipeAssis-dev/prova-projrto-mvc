# RESPOSTAS.md — Prova Prática PetVida

**Aluno:** Felipe
**NN:** 52

## Parte A — Construção do sistema

Evidências de execução das três rotas e do H2 Console:

- Ficha do animal (`/ficha_52`): ![ficha](evidencias/FichaAnimalEvi.png)
- Ficha do tutor (`/tutor_52`): ![tutor](evidencias/FichaTutorEvi.png)
- Resumo da clínica (`/resumo_52`): ![resumo](evidencias/ResumoClinicaEvi.png)
- Console H2 com SELECT do tutor 152 e animal 152: ![h2](evidencias/EvidenciaH2AnimalTutor.png)


## Parte B — Planejamento da camada de controle

B.1 (2 a 4 linhas) Qual das duas informações que identificam uma requisição HTTP — o verbo ou a URL — o Spring usa para escolher entre os métodos ficha() e resumo() do seu Controller? Por que a outra informação sozinha não bastaria?

Seria uma combinação entre ambos, mas se for para escolher qual indica de verdade entre ficha() e resumo() é a URL. Porque no projeto ficha_52 e resumo_52 usam o mesmo verbo (GET), então só o verbo não é o suficiente para o programa saber qual seria o metodo chamar quando as duas requisições chegam. O verbo sozinho reduziria as opções a "qualquer rota GET", mas ainda existiriam várias, é a URL que aponta exatamente para um único.