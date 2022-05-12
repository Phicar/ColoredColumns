pp=[]
L=[]
LL= []
ss =0
kk =0
nn=0
bk = 0
def go(a):
	global LL,pp,ss,kk,nn,bk
	if a==bk:
		paila = False
		for i in range(0,nn-1):
			#print(LL[i],LL[i+1])
			if LL[i]==LL[i+1]:
				paila = True
				break
		if not paila:
			#print(pp,LL)
			ss+=1
		return
	for i in range(0,kk):
		for x in pp[a]:
			LL[x-1]=i
		go(a+1)
def tal(n,co):
	tot = 0
	global pp,L,LL,ss,kk,bk,nn
	P = SetPartitions(n)
	nn = n
	kk = co
	for p in P:
		if not p.is_noncrossing():
			continue
		L = [0 for i in range(n)]
		k = 0
		bk = len(p)
		pp =p
		for b in p:
			k+=1
			for x in b:
				L[x-1]=k-1
		paila = False
		for i in range(n-1):
			if L[i]==L[i+1]:
				paila = True
				break
		if not paila:
			ss =0
			LL = [-1 for i in range(n)]
			go(0)
			tot+=ss
	return tot
def A(n):
        return binomial(n+floor(n/2.0),ceil(n/2.0))/(2*floor(n/2.0)+1)
def Col(n,k):
	s = 0
	for l in range(1,n+1):
		s+=binomial(n-1,l-1)*tal(l,k)
	return s
def Col2(n): # number of columns n, two colors.
        s =0
        for l in range(1,n+1):
                s+=binomial(n-1,l-1)*A(l)
        return 2*s
def Up(n,kk):
	s = 0
	for l in range(1,n+1):
		for k in range(1,l+1):
			s+=binomial(n-1,l-1)*binomial(l,k)*binomial(l,k-1)*(kk-1)**k/l
	return kk*s
def Low(n,kk):
	s = 0
	for l in range(1,n+1):
		s+=binomial(n-1,l-1)*catalan_number(l)
	return s*kl
kl = 5
for n in range(1,20):
	t = Col(n,kl)
	print(Low(n,kl)/kl,t/kl,Up(n,kl)/kl)
##Brute force of columns, Up is upper bound.
