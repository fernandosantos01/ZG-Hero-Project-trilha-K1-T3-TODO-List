const CHAVE_STORAGE = 'todolist_tarefas';

const REGEX_TITULO     = /^.{3,100}$/;
const REGEX_DESCRICAO  = /^[\s\S]{5,}$/;
const REGEX_PRIORIDADE = /^[1-5]$/;
const REGEX_DATA       = /^(0[1-9]|[12]\d|3[01])\/(0[1-9]|1[0-2])\/(\d{4})$/;

let listaTarefas = [];
let contadorId = 1;

const formularioTarefa     = document.getElementById('formulario-tarefa');
const inputId              = document.getElementById('id-tarefa');
const inputTitulo          = document.getElementById('titulo');
const inputDescricao       = document.getElementById('descricao');
const inputStatus          = document.getElementById('status');
const inputPrioridade      = document.getElementById('prioridade');
const inputDataTermino     = document.getElementById('data-termino');
const tituloFormulario     = document.getElementById('titulo-formulario');
const botaoCancelar        = document.getElementById('botao-cancelar');
const elementoListaTarefas = document.getElementById('lista-tarefas');
const selectFiltroStatus   = document.getElementById('filtro-status');
const controleSelecao      = document.getElementById('controle-selecao');
const selecionarTodas      = document.getElementById('selecionar-todas');
const barraSelecao         = document.getElementById('barra-selecao');
const contadorSelecionadas = document.getElementById('contador-selecionadas');
const statusMassa          = document.getElementById('status-massa');
const botaoAplicarMassa    = document.getElementById('botao-aplicar-massa');
const botaoLimparSelecao   = document.getElementById('botao-limpar-selecao');

const erroTitulo      = document.getElementById('erro-titulo');
const erroDescricao   = document.getElementById('erro-descricao');
const erroPrioridade  = document.getElementById('erro-prioridade');
const erroDataTermino = document.getElementById('erro-data-termino');


function salvarNoStorage() {
    localStorage.setItem(CHAVE_STORAGE, JSON.stringify(listaTarefas));
}

function carregarDoStorage() {
    const dados = localStorage.getItem(CHAVE_STORAGE);
    if (dados) {
        listaTarefas = JSON.parse(dados);
        const maiorId = listaTarefas.reduce((max, t) => Math.max(max, t.id), 0);
        contadorId = maiorId + 1;
    }
}


function exibirErro(span, campo, msg) {
    span.textContent = msg;
    campo.classList.add('campo-erro');
}

function limparErro(span, campo) {
    span.textContent = '';
    campo.classList.remove('campo-erro');
}

function dataStringParaDate(str) {
    const [dia, mes, ano] = str.split('/').map(Number);
    return new Date(ano, mes - 1, dia);
}

function dataEhValida(str) {
    if (!REGEX_DATA.test(str)) return false;
    const data = dataStringParaDate(str);
    const [dia, mes, ano] = str.split('/').map(Number);
    return data.getDate() === dia && data.getMonth() === mes - 1 && data.getFullYear() === ano;
}

function dataEhFuturaOuHoje(str) {
    const data = dataStringParaDate(str);
    const hoje = new Date();
    hoje.setHours(0, 0, 0, 0);
    return data >= hoje;
}

function validarFormulario() {
    let valido = true;

    if (!REGEX_TITULO.test(inputTitulo.value.trim())) {
        exibirErro(erroTitulo, inputTitulo, 'O título deve ter entre 3 e 100 caracteres.');
        valido = false;
    } else {
        limparErro(erroTitulo, inputTitulo);
    }

    if (!REGEX_DESCRICAO.test(inputDescricao.value.trim())) {
        exibirErro(erroDescricao, inputDescricao, 'A descrição deve ter pelo menos 5 caracteres.');
        valido = false;
    } else {
        limparErro(erroDescricao, inputDescricao);
    }

    if (!REGEX_PRIORIDADE.test(inputPrioridade.value.trim())) {
        exibirErro(erroPrioridade, inputPrioridade, 'Prioridade deve ser um número entre 1 e 5.');
        valido = false;
    } else {
        limparErro(erroPrioridade, inputPrioridade);
    }

    const dataVal = inputDataTermino.value.trim();
    if (!dataVal) {
        exibirErro(erroDataTermino, inputDataTermino, 'Data de término é obrigatória.');
        valido = false;
    } else if (!dataEhValida(dataVal)) {
        exibirErro(erroDataTermino, inputDataTermino, 'Data inválida. Use o formato dd/mm/aaaa (ex: 31/12/2026).');
        valido = false;
    } else if (!dataEhFuturaOuHoje(dataVal)) {
        exibirErro(erroDataTermino, inputDataTermino, 'A data de término não pode ser anterior a hoje.');
        valido = false;
    } else {
        limparErro(erroDataTermino, inputDataTermino);
    }

    return valido;
}


formularioTarefa.addEventListener('submit', function (evento) {
    evento.preventDefault();

    if (!validarFormulario()) return;

    const idAtual = inputId.value;
    const novaTarefa = {
        titulo:      inputTitulo.value.trim(),
        descricao:   inputDescricao.value.trim(),
        status:      inputStatus.value,
        prioridade:  parseInt(inputPrioridade.value.trim()),
        dataTermino: inputDataTermino.value.trim()
    };

    if (idAtual) {
        const indice = listaTarefas.findIndex(t => t.id === parseInt(idAtual));
        if (indice !== -1) {
            listaTarefas[indice] = { ...novaTarefa, id: parseInt(idAtual) };
        }
    } else {
        novaTarefa.id = contadorId++;
        listaTarefas.push(novaTarefa);
    }

    salvarNoStorage();
    resetarFormulario();
    renderizarTarefas();
});

function prepararEdicao(id) {
    const tarefa = listaTarefas.find(t => t.id === id);
    if (!tarefa) return;

    inputId.value          = tarefa.id;
    inputTitulo.value      = tarefa.titulo;
    inputDescricao.value   = tarefa.descricao;
    inputStatus.value      = tarefa.status;
    inputPrioridade.value  = tarefa.prioridade || '';
    inputDataTermino.value = tarefa.dataTermino || '';
    tituloFormulario.textContent = 'Editar Tarefa';
    botaoCancelar.classList.remove('oculto');
    formularioTarefa.scrollIntoView({ behavior: 'smooth' });
}

function excluirTarefa(id) {
    if (!confirm('Tem certeza que deseja excluir esta tarefa?')) return;
    listaTarefas = listaTarefas.filter(t => t.id !== id);
    salvarNoStorage();
    renderizarTarefas();
}

function resetarFormulario() {
    formularioTarefa.reset();
    inputId.value = '';
    tituloFormulario.textContent = 'Nova Tarefa';
    botaoCancelar.classList.add('oculto');

    [erroTitulo, erroDescricao, erroPrioridade, erroDataTermino]
        .forEach(e => e.textContent = '');
    [inputTitulo, inputDescricao, inputPrioridade, inputDataTermino]
        .forEach(c => c.classList.remove('campo-erro'));
}


function getIdsSelecionados() {
    return [...document.querySelectorAll('.checkbox-tarefa:checked')]
        .map(cb => parseInt(cb.dataset.id));
}

function atualizarBarraSelecao() {
    const ids = getIdsSelecionados();
    const total = ids.length;

    if (total > 0) {
        barraSelecao.classList.remove('oculto');
        contadorSelecionadas.textContent =
            `${total} tarefa${total > 1 ? 's' : ''} selecionada${total > 1 ? 's' : ''}`;
    } else {
        barraSelecao.classList.add('oculto');
    }

    const checkboxes = document.querySelectorAll('.checkbox-tarefa');
    selecionarTodas.checked = checkboxes.length > 0 && total === checkboxes.length;
}

function alterarStatusEmMassa() {
    const ids = getIdsSelecionados();
    if (ids.length === 0) return;

    const novoStatus = statusMassa.value;
    listaTarefas = listaTarefas.map(t =>
        ids.includes(t.id) ? { ...t, status: novoStatus } : t
    );

    salvarNoStorage();
    renderizarTarefas();
}

selecionarTodas.addEventListener('change', function () {
    document.querySelectorAll('.checkbox-tarefa')
        .forEach(cb => cb.checked = this.checked);
    atualizarBarraSelecao();
});

botaoAplicarMassa.addEventListener('click', alterarStatusEmMassa);

botaoLimparSelecao.addEventListener('click', function () {
    document.querySelectorAll('.checkbox-tarefa').forEach(cb => cb.checked = false);
    selecionarTodas.checked = false;
    barraSelecao.classList.add('oculto');
});


const LABELS_PRIORIDADE = ['', 'Mínima', 'Baixa', 'Média', 'Alta', 'Crítica'];

function renderizarTarefas() {
    elementoListaTarefas.innerHTML = '';
    selecionarTodas.checked = false;
    barraSelecao.classList.add('oculto');

    const filtroAtual = selectFiltroStatus.value;
    const tarefasFiltradas = filtroAtual === 'TODOS'
        ? listaTarefas
        : listaTarefas.filter(t => t.status === filtroAtual);

    controleSelecao.classList.toggle('oculto', tarefasFiltradas.length === 0);

    if (tarefasFiltradas.length === 0) {
        const mensagem = document.createElement('li');
        mensagem.className = 'mensagem-vazia';
        mensagem.textContent = 'Nenhuma tarefa encontrada.';
        elementoListaTarefas.appendChild(mensagem);
        return;
    }

    tarefasFiltradas.forEach(tarefa => {
        const item = document.createElement('li');
        const prio = tarefa.prioridade;
        const prioLabel = prio ? LABELS_PRIORIDADE[prio] : '—';
        const dataTermino = tarefa.dataTermino || '—';

        item.innerHTML = `
            <div class="selecao-tarefa">
                <input type="checkbox" class="checkbox-tarefa" data-id="${tarefa.id}">
            </div>
            <div class="detalhes-tarefa">
                <strong>${tarefa.titulo}</strong>
                <p>${tarefa.descricao}</p>
                <div class="badges">
                    <span class="status-badge status-${tarefa.status}">${tarefa.status}</span>
                    <span class="prioridade-badge prioridade-${prio}">P${prio || '—'} ${prioLabel}</span>
                    <span class="data-badge">📅 ${dataTermino}</span>
                </div>
            </div>
            <div class="acoes-tarefa">
                <button class="botao-editar" onclick="prepararEdicao(${tarefa.id})">Editar</button>
                <button class="botao-excluir" onclick="excluirTarefa(${tarefa.id})">Excluir</button>
            </div>
        `;

        item.querySelector('.checkbox-tarefa').addEventListener('change', function () {
            item.classList.toggle('selecionada', this.checked);
            atualizarBarraSelecao();
        });

        elementoListaTarefas.appendChild(item);
    });
}


botaoCancelar.addEventListener('click', resetarFormulario);
selectFiltroStatus.addEventListener('change', renderizarTarefas);

carregarDoStorage();
renderizarTarefas();
