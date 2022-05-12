nn=0
col = set()
mm = 0
kk = 0
L = []
s = 0
h=0
si = 0
D = [[0,1],[1,0],[0,-1],[-1,0]]
def A(n):
	return binomial(n+floor(n/2.0),ceil(n/2.0))/(2*floor(n/2.0)+1)
def Col2(n): # number of columns n, two colors.
	s =0
	for l in range(1,n+1):
		s+=binomial(n-1,l-1)*A(l)
	return 2*s
def dfs(a,b,c,d):
	global vis,D,L,nn,mm
	vis[a][b]=d
	for p in D:
		aa = a+p[0]
		bb = b+p[1]
		if 0<=aa and aa<nn and 0<=bb and bb<mm:
			if vis[aa][bb]==0 and L[aa][bb]==c:
				dfs(aa,bb,c,d)
def ComputePol(): #L is already fixed
	global L,nn,mm,vis,col,kk
	si = 0
	vis = [[0 for i in range(0,mm)] for j in range(0,nn)]
	for i in range(0,nn):
		for j in range(0,mm):
			if vis[i][j]==0:
				si+=1
				dfs(i,j,L[i][j],si)
	hs = 0
	hss = 0
	for i in range(0,len(vis[0])):
		hs=(nn*mm+1)*hs+vis[0][i]
	for i in range(0,len(vis[0])):
		hss=(kk+1)*hss+L[0][i]
	cs = len(col)
	col.add((hs,hss))
	#if len(col)>cs:
	#	print(len(col),vis[0],L[0])
	return si
def go(a,b):
	global vis,nn,mm,kk,L,s,h,si
	if a==nn:
		#print(L)
		si = ComputePol()
		#print(L,"\n",vis)
		s+=si
		return
	for i in range(1,kk+1):
		L[a][b]=i
		if b==mm-1:
			go(a+1,0)
		else:
			go(a,b+1)
def bf(n,m,k): #gives the average over all. Of Pol used and holes.
	global nn,mm,kk,L,s,h,col
	col = set()
	nn = n
	mm = m
	kk = k
	h = 0
	s = 0
	L = [[0 for j in range(0,m)] for i in range(0,n)]
	go(0,0)
	print("Columns "+str(len(col))+" "+str(len(col)/3))
	#print((s/(k**(n*m))).n(),(h/(k**(n*m))).n())
	return s/k**(n*m)
def Monte(n,m,k,hmt):
	global L,nn,mm,s,h,kk
	ss =0
	nn =n
	mm = m
	kk = k
	XX = [1/k for j in range(0,k)]
	X = GeneralDiscreteDistribution(XX)
	H = [0 for i in range(1000000)]
	ma = 0
	mi = 1000000000
	for r in range(0,hmt):
		L = [[1+X.get_random_element() for j in range(0,mm)] for i in range(0,nn)]
		#print(L)
		h = ComputePol()
		H[h]+=1.0 #/k**(n*m)
		ma = max(ma,h)
		mi = min(mi,h)
		ss+=h
	HH = []
	#for i in range(max(mi-10,0),ma+10):
	#	HH.append((i,H[i]))
	#points(HH).save("histo"+str(n)+"-"+str(m)+"-"+str(k)+".png")
	#print(ss/(1.0*hmt),ss,hmt)
	return ss/(1.0*hmt)
def ComputeHoles():
	global L,nn,mm
	h =0
	ll = -1
	cl = -1
	for i in range(0,nn):
		if L[i][0]==L[i][1] and L[i][0]==L[i][2]:
			if i>0 and ll>-1 and i-1>ll and cl==L[i][0]:
				sizas  =True
				for j in range(ll+1,i):
					if L[j][0]!=L[i][0] or L[j][2]!=L[i][2]:
						sizas = False
				if sizas:
					h+=1
			ll=i
			cl = L[i][0]
	return h
def MonteHoles(n,m,k,hmt):
        global L,nn,mm,s,h,kk
        ss =0
        nn =n
        mm = m
        kk = k
        XX = [1/k for j in range(0,k)]
        X = GeneralDiscreteDistribution(XX)
        H = [0 for i in range(1000000)]
        ma = 0
        mi = 1000000000
        for r in range(0,hmt):
                L = [[1+X.get_random_element() for j in range(0,mm)] for i in range(0,nn)]
                #print(L,)
                h = ComputeHoles()
		#print(h)
                H[h]+=1.0 #/k**(n*m)
                ma = max(ma,h)
                mi = min(mi,h)
                ss+=h
        HH = []
        #for i in range(max(mi-10,0),ma+10):
        #        HH.append((i,H[i]))
        #points(HH).save("holes"+str(n)+"-"+str(m)+"-"+str(k)+".png")
        return ss/hmt
def randomHolesExact(n):
	return ((7*n-15)+1/8**(n-2))/1568
def plotHistoFunc(n,m):
	LL = []
	for k in range(1,n*m+1):
		mm = Monte(n,m,k,9000)
		LL.append((k,mm/(n*m)))
		print(k,mm)
	G = points(LL) #+plot(log(x)/log(100),x,1,100,color='red')
	G.save("funck"+str(n)+"-"+str(m)+".png")
def plotHistoFixk(n,k): #the number of colors is fixed, columns move.
        LL = []
	LLL = 0
	b = 0
        for m in range(1,100):
                mm = Monte(m,n,k,2000)
		if m>1:
			LLL+=mm-b
                LL.append((m,mm))
                print(m,mm,0.88348*(m-1)+(((k-1)*n+1)/k),mm-b)
		b=mm
        G = points(LL)+plot((LLL/98.0)*(x-1)+(((k-1)*n+1)/k),x,1,100,color='red')
        G.save("funcm"+str(n)+"-"+str(k)+".png")
	return (LLL/98.0,(((k-1)*n+1)/k))
def plotHistoFixkAll(nn,k): #the number of colors is fixed, columns move.
	G = Graphics()
        for n in range(3,nn+1):
		LL = []
        	LLL = 0
       		b = 0
        	for m in range(1,100):
                	mm = Monte(m,n,k,1500)
                	if m>1:
                        	LLL+=mm-b
                	LL.append((m,mm))
                	#print(m,mm,0.88348*(m-1)+(((k-1)*n+1)/k),mm-b)
                	b=mm
        	G += points(LL)+plot((LLL/98.0)*(x-1)+(((k-1)*n+1)/k),x,1,100,color='red')
		print(n,LLL/98.0,(((k-1)*n+1)/k))
        G.save("fAll3"+str(nn)+".png")
        return (LLL/98.0,(((k-1)*n+1)/k))
