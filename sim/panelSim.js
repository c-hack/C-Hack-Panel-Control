var BASE_FILE_NAME = "panel%.dat";

function Led() {
    this.color = { r:0, g:0, b:0 };
    this.element = document.createElement("div");
    this.element.classList.add('led');
    this.element.style.backgroundColor = "rgb(" + 255 * this.color.r + "," + 255 * this.color.g + "," + 255 * this.color.b + ")";
}

Led.prototype.setColor = function(color) {
    this.color = color;
    this.element.style.backgroundColor = "rgb(" + 255 * this.color.r + "," + 255 * this.color.g + "," + 255 * this.color.b + ")";
}

function TableCell() {
    this.led = new Led();
    this.element = document.createElement("td");
    this.element.appendChild(this.led.element);
}

function TableRow() {
    this.cells = Array();
    for(let i = 0; i < 4; i ++) {
      this.cells[i] = new TableCell();
    }
    this.element = document.createElement("tr");
    for(c in this.cells) {
        this.element.appendChild(this.cells[c].element);
    }
}

function Table() {
    this.rows = Array();
    for(let i = 0; i < 7; i ++) {
      this.rows[i] = new TableRow();
    }
    this.element = document.createElement("table");
    for(r in this.rows) {
        this.element.appendChild(this.rows[r].element);
    }
}

function Panel(id) {
    this.id = id;
    this.table = new Table();
    this.element = document.getElementById("panel" + id);
    this.element.appendChild(this.table.element);
    this.leds = Array();
    for(let i = 0; i < this.table.rows.length; i ++) {
      this.leds[i] = Array(); 
      row = this.table.rows[i];
      for(let j = 0; j < row.cells.length; j ++) {
          this.leds[i][j] = row.cells[j].led;
      }
    }
}

Panel.prototype.loadData = function() {
    let me = this;
    let xhttp = new XMLHttpRequest();
    xhttp.onload = function() {
        if (this.readyState == 4 && this.status == 200) {
            leds = new Array();
            rows = this.responseText.split("\n");
            if(rows.length != 7) return;
            for(r in rows) {
                leds[r] = new Array();
                cells = rows[r].split(" ");
                if(cells.length != 4) return;
                for(c in cells) {
                    leds[r][c] ={};
                    leds[r][c].r = cells[c].substring(0,1);
                    leds[r][c].g = cells[c].substring(1,2);
                    leds[r][c].b = cells[c].substring(2);
                }
            }
            console.log(leds);
            for(i in leds) {
                for(j in leds[i]) {
                    me.leds[i][j].setColor(leds[i][j]);
                }
            }
        }
    };
    let name = BASE_FILE_NAME.replace("%", me.id);
    xhttp.responseType = "text";
    xhttp.open("GET", name, true);
    xhttp.send(null);
}

let panels = Array();

function run() {
    for(let i = 1; i <= 6; i ++) {
      panels[i] = new Panel(i)
    }
    window.setInterval(loadAll, 100);
}

function loadAll() {
    for(let i = 1; i <= 6; i ++) {
      panels[i].loadData();
    }
}
