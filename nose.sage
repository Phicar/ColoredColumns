for n in range(1,20):
	s = 0
	for l in range(n,n+1):
		s+=catalan_number(n) #2*binomial(n-1,l-1)*catalan_number(l)
	print(n,2*s)
