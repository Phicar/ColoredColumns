R = PolynomialRing(QQ,"xy",2)
y = polygen(QQ, "y")
x = PowerSeriesRing(y.parent(), "x").gen()
#### Comentar si quiero expansion las dos siguientes lineas
x = R("x")
y = R("y")
#me da cuantos elementos por imagen i.e., (|f^(-1)(1)|,...,|f^(-1)(k)|)
def func(a,b):
        if a==0:
                return [[]]
        tal = func(a-1,b)
        tale =[]
        for x in tal:
                for y in range(b):
                        tale.append(x+[y])
        return tale
def funcToVec(x,n,k):
	v = [0 for i in range(k)]
	for y in x:
		v[y]+=1
	return sorted(v)
def delta(a,b):
	if a==b:
		return 1
	return 0
def invCl(t,n,k): #recibe tupla retorna function
	ft = [0 for i in range(n)]
	ini = 0
	for i in range(k):
		for j in range(t[i]):
			ft[ini]=i
			ini+=1
	#print(t,ft)
	return ft
def nuevPart(k,aa,bb):
	p = 0
	for i in range(k):
		sa = set()
		sb = set()
		entro = False
		for j in range(len(aa)):
			if aa[j]==i:
				sa.add(j)
			if bb[j]==i:
				sb.add(j)
				entro = True
		#print(sa,sb)
		if entro and len(sa&sb)==0:
			p+=1
	#print(k,aa,bb,p)
	return p
def genMatrixNK(n,k):
	fnk = func(n,k)
	eqvCl = dict()
	lisRepreEq = []
	for f in fnk:
		#print(f)
		tf = tuple(funcToVec(f,n,k))
		if tf not in eqvCl.keys():
			eqvCl[tf] = [f]
			lisRepreEq.append(tf)
		else:
			eqvCl[tf].append(f)
	dim = len(lisRepreEq)
	A = [[0 for i in range(dim)] for j in range(dim)]
	I = [[delta(i,j) for j in range(dim)] for i in range(dim)]
	casBase = [0 for i in range(dim)]
	for i in range(dim):
		sup = 0
		t = lisRepreEq[i]
		for j in range(len(t)):
			if t[j]>0:
				sup+=1
		casBase[i] = x*(y**sup)
		for j in range(dim):
			#print(t,lisRepreEq[j])
			for ff in eqvCl[lisRepreEq[j]]:
				#print(t,lisRepreEq[j])
				A[i][j]+=x*(y**(nuevPart(k,ff,invCl(t,n,k))))
	#print(A)
	#print(casBase)
	AI = (matrix(I)-matrix(A)).inverse()
	AIm = AI*(matrix(casBase).transpose())
	R = 0
	#you weight each variable by the size of its equiv class
	for i in range(dim):
		R+=len(eqvCl[lisRepreEq[i]])*AIm[i]
	return R
def verGen(sn,k):
    Ress = genMatrixNK(sn,k)
    print("genFunc:",Ress)
    expVS = (Ress.derivative(y))(x,1)
    print("expected Value:",expVS)
@interact
def _(sn = input_box('3',type= int ,label='n'),k=input_box('2',type= int ,label='k')):
    global x,y
    verGen(sn,k)
