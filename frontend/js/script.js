let listaTarefas = [];
let contadorId = 1;

const formularioTarefa = document.getElementById('formulario-tarefa');
const inputId = document.getElementById('id-tarefa');
const inputTitulo = document.getElementById('titulo');
const inputDescricao = document.getElementById('descricao');
const inputStatus = document.getElementById('status');
const tituloFormulario = document.getElementById('titulo-formulario');
const botaoCancelar = document.getElementById('botao-cancelar');
const elementoListaTarefas = document.getElementById('lista-tarefas');
const selectFiltroStatus = document.getElementById('filtro-status');

formularioTarefa.addEventListener('submit', function(evento) {
    evento.preventDefault();

    const idAtual = inputId.value;
    const novaTarefa = {
        titulo: inputTitulo.value,
        descricao: inputDescricao.value,
        status: inputStatus.value
    };

    if (idAtual) {
        const indiceTarefa = listaTarefas.findIndex(tarefa => tarefa.id == idAtual);
        if (indiceTarefa !== -1) {
            listaTarefas[indiceTarefa] = { ...novaTarefa, id: parseInt(idAtual) };
        }
        resetarFormulario();
    } else {
        novaTarefa.id = contadorId++;
        listaTarefas.push(novaTarefa);
    }

    resetarFormulario();
    renderizarTarefas();
});

function prepararEdicao(id) {
    const tarefaParaEditar = listaTarefas.find(tarefa => tarefa.id === id);
    if (tarefaParaEditar) {
        inputId.value = tarefaParaEditar.id;
        inputTitulo.value = tarefaParaEditar.titulo;
        inputDescricao.value = tarefaParaEditar.descricao;
        inputStatus.value = tarefaParaEditar.status;

        tituloFormulario.textContent = "Editar Tarefa";
        botaoCancelar.classList.remove('oculto');
    }
}

function excluirTarefa(id) {
    if (confirm("Tem certeza que deseja excluir esta tarefa?")) {
        listaTarefas = listaTarefas.filter(tarefa => tarefa.id !== id);
        renderizarTarefas();
    }
}

function renderizarTarefas() {
    elementoListaTarefas.innerHTML = '';
    const filtroAtual = selectFiltroStatus.value;

    const tarefasFiltradas = filtroAtual === 'TODOS' 
        ? listaTarefas 
        : listaTarefas.filter(tarefa => tarefa.status === filtroAtual);

    tarefasFiltradas.forEach(tarefa => {
        const itemLista = document.createElement('li');

        itemLista.innerHTML = `
            <div class="detalhes-tarefa">
                <strong>${tarefa.titulo}</strong>
                <p style="margin: 5px 0;">${tarefa.descricao}</p>
                <span class="status-badge status-${tarefa.status}">${tarefa.status}</span>
            </div>
            <div class="acoes-tarefa">
                <button class="botao-editar" onclick="prepararEdicao(${tarefa.id})">Editar</button>
                <button class="botao-excluir" onclick="excluirTarefa(${tarefa.id})">Excluir</button>
            </div>
        `;
        elementoListaTarefas.appendChild(itemLista);
    });
}

function resetarFormulario() {
    formularioTarefa.reset();
    inputId.value = '';
    tituloFormulario.textContent = "Nova Tarefa";
    botaoCancelar.classList.add('oculto');
}

botaoCancelar.addEventListener('click', resetarFormulario);
selectFiltroStatus.addEventListener('change', renderizarTarefas);