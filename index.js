function genHist() {
  tno = parseInt(document.getElementById("transno").value);
  hno = parseInt(document.getElementById("hno").value);

  if (hno > 4) {
    alert("H No should be less than 4");
    return;
  }
  if (tno > 4) {
    alert("Transaction No should be less than 4");
    return;
  }

  trans = ["T1", "T2", "T3", "T4"];
  ops = ["R", "W",];
  datas = ["A", "B", "C", "D"];
  spOps = ["C", "A"];
  hist = [];

  for (var i = 1; i <= tno; i++) {
    ops.forEach(function(op) {
      for (var j = 0; j < hno; j++) {
        var data = datas[j];
        hist.push(op + i + "(" + data + ")");
      }
    });
    var random = Math.floor((Math.random() * 10) % 2);
    spOp = spOps[random];
    hist.push(spOp + i);
  }

  shuffleArray(hist);
  document.getElementById("history").innerText = hist;
}

function analyze() {
  document.getElementById("analysis").innerText = "";
  checkIfSerializable();
  checkIfRecoverable();
  checkIfStrict();
  checkIfACA();
}

function checkIfRecoverable() {
  if (tno == 1) {
    document.getElementById("analysis").innerText += "Recoverable\r\n";
    return;
  }
  var ascs = getAllAbortsAndCommits();
  for (var i = 0; i < tno; i++) {
    if (ascs[i].substring(0, 1) == "A") {

    }
  }
}

function checkIfStrict() {
     /*If in the given schedule, each transaction Tj neither reads nor
     writes any data item ‘x’ until the last transaction Ti
     that has written ‘X’ is committed or aborted then it is strict.*/
     var ACA = true;
     //get the data items written by T1 and commit or abort position
     for (var j = 1; j <= 4; j++) {
         var Find = j;
         var CALocation = 0;
         var list = [];
         for (var i = 0; i < hist.length; i++) {
             if (hist[i].indexOf(Find) > -1) {

                 if (hist[i].indexOf("A") == 0 || hist[i].indexOf("C") == 0) {
                     CALocation = i;
                     break;
                 } else {
                     if (hist[i].indexOf("W") > -1) {
                         list.push(hist[i].substring(3, 4));
                     }
                 }
             }
         }

         for (var i = 0; i < CALocation; i++) {
             if (hist[i].indexOf(Find) > -1 || hist[i].length == 2) {
                 continue;
             }

             var temp = hist[i].substring(3, 4);
             if (list.indexOf(temp) > -1) {
                 ACA = false;
                 j = 10;
                 break;
             }

         }

     }
     if (ACA) {
       document.getElementById("analysis").innerText += "Strict\r\n";
     } else {
       document.getElementById("analysis").innerText += "Not Strict\r\n";
     }
 }

function checkIfACA() {
    var Strict = true;
    //get the data items written by T1 and commit or abort position
    for (var j = 1; j <= 4; j++) {
        var Find = j;
        var CALocation = 0;
        var list = [];
        for (var i = 0; i < hist.length; i++) {
            if (hist[i].indexOf(Find) > -1) {
                if (hist[i].indexOf("A") == 0 || hist[i].indexOf("C") == 0) {
                    CALocation = i;
                    break;
                } else {
                    if (hist[i].indexOf("W") > -1) {
                        list.push(hist[i].substring(3, 4));
                    }
                }
            }
        }

        for (var i = 0; i < CALocation; i++) {
            if (hist[i].indexOf(Find) > -1 || hist[i].length == 2) {
                continue;
            }

            var temp = hist[i].substring(3, 4);

            if (list.indexOf(temp) > -1) {
                if (hist[i].indexOf("R") > -1) {
                    Strict = false;
                    j = 10;
                    break;
                }
            }
        }
    }

    if (Strict) {
      document.getElementById("analysis").innerText += "ACA\r\n";
    } else {
      document.getElementById("analysis").innerText += "Not ACA\r\n";
    }
}

function getAllAbortsAndCommits() {
  var ascs = [];
  hist.forEach(function(h) {
    if (h.length == 2) {
      ascs.push(h);
    }
  });
  return ascs;
}

function checkIfCyclic(hash) {
  var cyclic = false;
  for (var i = 0; i < tno; i++) {
    index_list = hash[trans[i]];
    if ((index_list[0] - index_list[1]) * -1 > 1) {
        document.getElementById("analysis").innerText = "Not Serializable as there is a cycle for " + trans[i] + " \r\n";
        cyclic = true;
        break;
    }
  }
  if (!cyclic)
    document.getElementById("analysis").innerText = "Serializable\r\n";
}

function checkIfSerializable() {
  for (var i = 0; i < hno; i++) {
      var data = datas[i];
      var opsOnData = findOpsFor(data);
      checkIfCyclic(opsOnData);
  }
}

function findOpsFor(data) {
  var opsOnData = {};
  var i = 0;
  hist.forEach(function(op) {
    if (op.indexOf(data) == 3) {
      var tran = trans[parseInt(op.substring(1, 2) - 1)];
      if (opsOnData[tran] == undefined)
        opsOnData[tran] = [];
      opsOnData[tran].push(i);
    }
    i++;
  });
  return opsOnData;
}

/**
 * Randomize array element order in-place.
 * Using Durstenfeld shuffle algorithm.
 */
function shuffleArray(array) {
    for (var i = array.length - 1; i > 0; i--) {
        var j = Math.floor(Math.random() * (i + 1));
        var temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
