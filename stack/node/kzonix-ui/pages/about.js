import withHeader from '../components/header/layout-fn'

const pageContent = () =
> <
p > About
us
! < /p>
export default function About () {
  return withHeader(pageContent)()
}
